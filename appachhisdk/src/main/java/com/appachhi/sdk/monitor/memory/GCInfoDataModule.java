package com.appachhi.sdk.monitor.memory;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.DataObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data Module providing information about the GC Operation happening in the current Process
 * <p>
 * The information is extracted from the Android Logcat and parsed.Caveat is that android no more
 * publishes GC information every time as it has now moved to ART Runtime.This module just support
 * ART as of now
 * <p>
 * You can find more information on GC Logcat information from
 * {@see <a href="https://developer.android.com/studio/debug/am-logcat">Android Documentation</a>}
 */
public class GCInfoDataModule extends BaseDataModule<GCInfo> implements Runnable {
    private static final String TAG = "GCInfoDataModule";
    private static final String LOGCAT_NAME = "logcat";
    private static final String LOGCAT_FILTER = "-s";
    private static final String LOGCAT_FILTER_BY_TAG_AND_PRIORITY = "*:I";

    /**
     * The Pattern which is used for parsing the Logcat Information
     * <p>
     * You can find more information about the logcat format from
     * {@see <a href="https://developer.android.com/studio/debug/am-logcat">Android Documentation</a> }
     */
    private static final Pattern GC_REGEX = Pattern.compile("^.* I[/\\s].*: (\\w*) (.*) freed ([\\d]+)\\((.*)\\) AllocSpace objects, ([\\d]+)\\((.*)\\) LOS objects, (.*) free, (.*)/(.*), paused (.*) total (.*)$", Pattern.CASE_INSENSITIVE);

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
            notifyObserver();
        }
    };

    @Nullable
    @Override
    protected GCInfo getData() {
        return data;
    }

    @Override
    public void start() {
        startLogcatProcess();
        executor.execute(this);
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
            Log.e(TAG, "Failed to start logcat Process", e);
        }
    }

    @Override
    public void stop() {
        stopLogcatProcess();
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

    @Override
    public void run() {
        Log.d(TAG, "Running Logcat GC Info Parsing");
        if (runningProcess != null) {
            parseLogAndNotify();

        }
    }

    /**
     * Read the information from the running logcat Process and parses and notify the
     * {@link DataObserver}
     */
    private void parseLogAndNotify() {
        try {
            String line;
            while (!isStreamClosed && brq != null && (line = brq.readLine()) != null) {
                if (!line.isEmpty()) {
                    GCInfo computedData = parseLogToGCInfo(line);
                    if (computedData != null) {
                        data = computedData;
                        mainHandler.post(notifyOnMainThread);
                    }

                }
            }

        } catch (IOException e) {
            // Handle Silently
        }
    }


    /**
     * Parses the Logcat's GC Information
     *
     * @param line Logcat Line
     * @return GCInfo Parsed GC Information when successfully parse and null if the the logcat text
     * could not be parsed
     */
    @Nullable
    private static GCInfo parseLogToGCInfo(@NonNull String line) {
        // Line is Empty
        if (line.isEmpty()) return null;

        Matcher regexMatcher = GC_REGEX.matcher(line);
        // Couldn't be parsed
        if (!regexMatcher.find()) return null;
        // Couldn't extract all information.Total Group Count is 11
        if (regexMatcher.groupCount() != 11) return null;

        return new GCInfo(regexMatcher.group(1),regexMatcher.group(2),
                regexMatcher.group(3), regexMatcher.group(4),
                regexMatcher.group(5), regexMatcher.group(6),
                regexMatcher.group(7), regexMatcher.group(8),
                regexMatcher.group(9), regexMatcher.group(10),
                regexMatcher.group(11));
    }
}
