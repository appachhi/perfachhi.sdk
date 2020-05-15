package com.appachhi.sdk.monitor.startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.appachhi.sdk.BaseDataModule;


public class StartupDataModule extends BaseDataModule<StartupTimeInfo> implements Runnable {


    public StartupTimeInfo startupTimeInfo;
    public SharedPreferences sharedPreferences;
    public StartupTimeManager startupTimeManager;
    public Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String TAG = "StartupDataModule";
    private String SHARED_PREFERENCE_NAME = "startupSharedPref";
    private SharedPreferences.Editor editor;


    public StartupDataModule(Context context) {
        this.context = context;

        startupTimeManager = new StartupTimeManager(context);


    }

    @Override
    protected StartupTimeInfo getData() {
        return startupTimeInfo;
    }

    @Override
    public void start() {
        handler.post(this);
        Log.d(TAG, "start: handler.post ");

    }

    @Override
    public void stop() {
        handler.removeCallbacks(this);
        Log.d(TAG, "stop: handler.removeCallbacks ");
    }

    @Override
    public void run() {

        long coldStartResult = startupTimeManager.getCold_startResult();
        long warmStartResult = startupTimeManager.getWarmStart_result();

        Log.d(TAG, "run:  ColdStart : " + coldStartResult + " Warm Start : " + warmStartResult);
        //This is the result that will be used as data to send.
        startupTimeInfo = new StartupTimeInfo(coldStartResult, warmStartResult);

        notifyObservers();

        //handler.postAtFrontOfQueue(this);
        stop();
    }
}