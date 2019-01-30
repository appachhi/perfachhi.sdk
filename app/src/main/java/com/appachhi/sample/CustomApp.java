package com.appachhi.sample;

import android.app.Application;

import com.appachhi.sdk.Appachhi;

public class CustomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Appachhi.init(this);
    }
}
