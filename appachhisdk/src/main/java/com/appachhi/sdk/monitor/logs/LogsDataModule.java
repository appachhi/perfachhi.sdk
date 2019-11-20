package com.appachhi.sdk.monitor.logs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogsDataModule extends BaseDataModule<LogsInfo> {

    private static final String TAG = "LogsDataModule";
    private static final String LOGCAT_NAME = "logcat";
    private static final String LOGCAT_FILE = "-f";
    // Executor to parse the adb logcat continuously
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private Executor logsExecutor = Executors.newSingleThreadExecutor();

    @Nullable
    private Process runningProcess;
    @NonNull
    private
    SessionManager sessionManager;
    @Nullable
    private
    Session currentlyLoggingSession;
    private LogsInfo logsInfo;
    private Context appContext;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);

    LogsDataModule(Context appContext, @NonNull SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.appContext = appContext;
    }


    @Nullable
    @Override
    protected LogsInfo getData() {
        return logsInfo;
    }

    @Override
    public void start() {
        mainThreadHandler.postDelayed(scheduleContinuousChecking, 5000);
        logsExecutor.execute(new Runnable() {
            @Override
            public void run() {
                startProcess();
            }
        });

    }

    @Override
    public void stop() {
        stopProcess();
        mainThreadHandler.removeCallbacks(scheduleContinuousChecking);
    }

    private void startProcess() {
        Session session = sessionManager.getCurrentSession();
        if (session != null) {
            try {
                Date startTime = new Date();
                File logFile = createLogFile(session.getId(), startTime);
                String logsFilePath = logFile.getAbsolutePath();
                // Clears the logcat and start fresh
                Runtime.getRuntime().exec("logcat -c").waitFor();
                final ProcessBuilder logcatProcessBuilder = new ProcessBuilder(
                        LOGCAT_NAME,
                        LOGCAT_FILE,
                        logsFilePath
                );
                Log.d(TAG, "Starting logging process " + logcatProcessBuilder.command().toString());
                runningProcess = logcatProcessBuilder.start();
                currentlyLoggingSession = session;
                logsInfo = new LogsInfo(logFile, startTime);
            } catch (IOException|InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopProcess() {
        // Destroy the logs export process and notify the user of the file
        if (runningProcess != null) {
            runningProcess.destroy();
            notifyObservers();
            logsInfo = null;
        }
    }

    private File createLogFile(String id, Date startTime) {
        String sb = id +
                "_" +
                dateFormat.format(startTime) +
                ".txt";
        File logFile = new File(appContext.getExternalFilesDir("logs"), sb);
        Log.d(TAG, "LOgs file is " + logFile.getAbsolutePath());
        return logFile;
    }

    private Runnable scheduleContinuousChecking = new Runnable() {
        @Override
        public void run() {
            if (logsInfo != null) {
                File currentLogFile = logsInfo.getLogFilePath();
                // This the logs are not continuously written in same file
                if (runningProcess != null && currentLogFile != null && currentLogFile.exists()) {
                    // If tht current log file size greater than 100KB split it into another log
                    if (currentLogFile.length() > (1024L * 10L)) {
                        // Restart a new process again, so this will begin writing to an new file
                        restartProcess();
                    }
                }
            }

            // When the logging is running for a session and developer has requested to change the session
            // or for some reason the current session is different from currently logging session,
            // then restart the logging process with the current session
            Session currentSession = sessionManager.getCurrentSession();
            if (currentlyLoggingSession != null && currentSession != null && !currentlyLoggingSession.getId().equals(currentSession.getId())) {
                restartProcess();
            }
            mainThreadHandler.postDelayed(this, 5000);
        }
    };

    private void restartProcess() {
        stopProcess();
        startProcess();
    }
}
