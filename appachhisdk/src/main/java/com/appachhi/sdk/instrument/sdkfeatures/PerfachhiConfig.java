package com.appachhi.sdk.instrument.sdkfeatures;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.FeatureConfigManager;
import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.monitor.fps.FpsFeatureModule;
import com.appachhi.sdk.sync.SessionManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public PerfachhiConfig(Context context) {
        this.context = context;
        Log.d("TAG", "Perfachhi Config() called");


        appachhi = Appachhi.getInstance();

    }
    
    /*public void setMemoryInfo (boolean status) {
        if (status) {
            appachhi.addMemoryInfoModule();
        }
    }


   public void setGC(boolean status) {
        if (status) {
            Log.d("TAG", "setGC: From PerfachhiConfig ");
            appachhi.addGCModule();
        }
   }


    public void setNetworkUsage(boolean status) {
        if (status) {
            Log.d("TAG", "setNetworkUsage: From PerfachhiConfig ");
            appachhi.addNetworkUsageModule();
        }
    }


    public void setCpuUsage(boolean status) {
        if (status) {
            Log.d("TAG", "setCpuUsage: From PerfachhiConfig ");
            appachhi.addCpuUsage();
        }
    }

    public void setFps (boolean status) {
        if (status) {
            Log.d("TAG", "setFps: From PerfachhiConfig");
            appachhi.addFpsModule();
            setFrameDrop(status);
        }
    }

    public void setMemoryLeak(boolean status) {
        if (status) {
            Log.d("TAG", "setMemoryLeak: From PerfachhiConfig ");
            appachhi.addMemoryLeakModule();
        }
    }

    public void setFrameDrop(boolean status) {
        if (status) {
            Log.d("TAG", "setFrameDrop: From PerfachhiConfig ");
            appachhi.addFrameDropModule();
        }
    }

    public void setScreenCapture(boolean status) {
        if (status) {
            Log.d("TAG", "setScreenCapture: From PerfachhiConfig ");
            appachhi.addScreenCaptureModule();
        }
    }*/


}
