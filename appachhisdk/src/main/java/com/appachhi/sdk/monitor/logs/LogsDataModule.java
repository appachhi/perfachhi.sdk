package com.appachhi.sdk.monitor.logs;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.monitor.memory.GCInfo;

import java.io.BufferedReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogsDataModule extends BaseDataModule<String> {

    private static final String TAG = "LogsDataModule";
    // Executor to parse the adb logcat continuously
    private static Executor executor = Executors.newSingleThreadExecutor();

    // Main Thread Handler to notify observer on the main thread
    @NonNull
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    @NonNull
    private ProcessBuilder logcatProcessBuilder = new ProcessBuilder(LOGCAT_NAME, LOGCAT_FILTER , LOGCAT_FILTER_BY_TAG_AND_PRIORITY);
    // Current GC Information
    @Nullable
    private GCInfo data;
    // Currently running Logcat Process
    @Nullable
    private java.lang.Process runningProcess;
    @Nullable
    private BufferedReader brq;
    // Field denoting whether the stream from Logcat Process has been closed or not
    private boolean isStreamClosed = true;

    // Runnable to notify the data changes to the observers
    private Runnable notifyOnMainThread = new Runnable() {
        @Override
        public void run() {
            notifyObservers();
        }
    };
    @Nullable
    @Override
    protected String getData() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    /**
     * Starts the Logcat Process and connects and Stream to a reader
     */
    private void startLogcatProcess() {
        try {
            Log.d(TAG, String.format("starting Logcat Process with %s", logcatProcessBuilder.command()));
            runningProcess = logcatProcessBuilder.start();
            brq = new BufferedReader(new InputStreamReader(runningProcess.getInputStream()), 8192);
            isStreamClosed = false;
            Log.d(TAG, "Process Started");
        } catch (IOException e) {
            Log.e(TAG, "Failed to startAndBind logcat Process", e);
        }
    }

    /**
     * Stops the started Logcat Process
     */
    private void stopLogcatProcess() {
        Log.d(TAG, "Stopping Logcat Process");
        if (runningProcess != null) {
            Log.d(TAG, "Logcat Process Stopped");
            runningProcess.destroy();
        }
    }
}
