package com.appachhi.sdk.memory;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.common.DataObserver;


/**
 * {@link MemoryInfo} Data Observer logging the information on to the logcat
 */
public class MemoryInfoDataObserver implements DataObserver<MemoryInfo> {
    private static final String TAG = "Appachhi-Memory";

    @MainThread
    @Override
    public void onDataAvailable(@NonNull MemoryInfo data) {
        Log.i(TAG, data.asJsonString());
    }
}
