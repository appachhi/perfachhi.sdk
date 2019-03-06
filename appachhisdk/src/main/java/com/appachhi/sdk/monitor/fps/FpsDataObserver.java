package com.appachhi.sdk.monitor.fps;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;

public class FpsDataObserver implements DataObserver<Double> {

    private static final String TAG = "Appachhi-FPS";

    @Override
    public void onDataAvailable(@NonNull Double data) {
        Log.d(TAG, "FPS - " + data);
    }
}
