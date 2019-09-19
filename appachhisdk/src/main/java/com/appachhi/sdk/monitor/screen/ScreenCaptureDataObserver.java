package com.appachhi.sdk.monitor.screen;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;

public class ScreenCaptureDataObserver implements DataObserver<String> {
    private static final String TAG = "ScreenCaptureObserver";

    @Override
    public void onDataAvailable(@NonNull String filePath) {
        Log.d(TAG, "Screen captured file saved at " + filePath);
    }
}
