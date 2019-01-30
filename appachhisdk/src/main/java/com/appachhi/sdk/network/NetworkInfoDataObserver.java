package com.appachhi.sdk.network;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.common.DataObserver;

/**
 * {@link NetworkInfo} data observer which logs the information on to the logcat
 */
public class NetworkInfoDataObserver implements DataObserver<NetworkInfo> {
    private static final String TAG = "Appachhi-Network";

    /**
     * Logs the {@link NetworkInfo} on to the logcat as Json string
     *
     * @param data published
     */
    @MainThread
    @Override
    public void onDataAvailable(@NonNull NetworkInfo data) {
        Log.i(TAG, data.asJsonString());
    }
}
