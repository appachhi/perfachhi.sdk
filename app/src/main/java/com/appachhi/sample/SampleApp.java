package com.appachhi.sample;

import android.app.Application;
import android.util.Log;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SampleApp","oncreate");
    }
}
