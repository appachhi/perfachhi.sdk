package com.appachhi.sdk.instrument.sdkfeatures;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.FeatureConfigManager;
import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.monitor.fps.FpsFeatureModule;
import com.appachhi.sdk.sync.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class PerfachhiConfig {

    public Context context;
    public Application application;
    List<FeatureModule> featureModules;

    public AppachhiDB db;
    private ExecutorService dbExecutor;
    public SessionManager sessionManager;

    Appachhi appachhi;

    public FpsFeatureModule fpsFeatureModule;
    public FeatureConfigManager featureConfigManager;

    public Activity activity;


    public String fps, gc, memory_leak;

    public PerfachhiConfig(Context context) {
        this.context = context;
        //this.activity = activity;
        Log.d("TAG", "Perfachhi Config() called");


        appachhi = Appachhi.getInstance();

    }



}
