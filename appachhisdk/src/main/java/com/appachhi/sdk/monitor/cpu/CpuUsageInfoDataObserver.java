package com.appachhi.sdk.monitor.cpu;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;

/**
 * {@link CpuUsageInfo} Data Observer logging the CPU Usage related information on to the logcat
 */
class CpuUsageInfoDataObserver implements DataObserver<CpuUsageInfo> {
    private static final String TAG = "Appachhi-CPU-Usage";

    @Override
    public void onDataAvailable(@NonNull CpuUsageInfo data) {
        Log.i(TAG, data.asJsonString());
    }
}
