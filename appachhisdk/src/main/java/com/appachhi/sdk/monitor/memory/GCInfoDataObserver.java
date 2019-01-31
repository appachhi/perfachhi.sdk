package com.appachhi.sdk.monitor.memory;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.monitor.common.DataObserver;

/**
 * {@link GCInfo} Data Observer logging the GC related information on to the logcat
 */
public class GCInfoDataObserver implements DataObserver<GCInfo> {
    private static final String TAG = "Appachhi-GC";

    /**
     * Logs {@link GCInfo} as Json String
     * @param data Data Published by {@link com.appachhi.sdk.monitor.common.DataModule}
     */
    @MainThread
    @Override
    public void onDataAvailable(@NonNull GCInfo data) {
        Log.i(TAG, data.asJsonString());
    }
}
