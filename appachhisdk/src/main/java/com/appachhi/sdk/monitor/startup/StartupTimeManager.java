package com.appachhi.sdk.monitor.startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class StartupTimeManager {

    public long cold_starttime;
    public long warm_starttime;
    public long coldStart_result;
    public long warmStart_result;
    public boolean firstLaunch;
    public boolean warmStartStatus;
    public Context context;
    private SharedPreferences startupSharedPref;
    private SharedPreferences.Editor startupSharedPrefEditor;
    private String SHARED_PREFERENCE_NAME = "startupSharedPref";
    private String TAG = "StartupTimeManager";

    public StartupTimeManager(Context context) {
        this.context = context;

        if (context != null) {

            Log.d(TAG, "StartupTimeManager: initialzied : " + context.getApplicationInfo());

        }
        if (startupSharedPref == null) {

            startupSharedPref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            Log.d(TAG, "StartupTimeManager: Shared preference : " + startupSharedPref.toString());
        }
    }


    public long getCold_starttime() {

        cold_starttime = startupSharedPref.getLong("cold_start_time", 00400);
        Log.d(TAG, "getCold_starttime: this.coldstart = " + cold_starttime);

        return cold_starttime;
    }

    public void setCold_starttime(long cold_starttime) {
        this.cold_starttime = cold_starttime;

        Log.d(TAG, "setCold_starttime: Value : " + this.cold_starttime);

        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putLong("cold_start_time", cold_starttime).apply();

    }

    public long getCold_startResult() {

        coldStart_result = startupSharedPref.getLong("cold_start_result", 0000);
        Log.d(TAG, "getCold_startResult: " + coldStart_result);
        return coldStart_result;
    }

    public void setCold_startResult(long cold_startResult) {
        this.coldStart_result = cold_startResult;

        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putLong("cold_start_result", coldStart_result).apply();
    }


    public long getWarm_starttime() {
        warm_starttime = startupSharedPref.getLong("warm_start_time", 0000);
        Log.d(TAG, "getWarmStartTime: " + warm_starttime);
        return warm_starttime;
    }

    public void setWarm_starttime(long warm_starttime) {
        this.warm_starttime = warm_starttime;

        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putLong("warm_start_time", warm_starttime).apply();


    }


    public long getWarmStart_result() {

        warmStart_result = startupSharedPref.getLong("warm_start_value", 0000);
        Log.d(TAG, "getWarmStart_Result: " + warmStart_result);
        return warmStart_result;
    }

    public void setWarmStart_result(long warmStart_result) {
        this.warmStart_result = warmStart_result;
        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putLong("warm_start_value", warmStart_result).apply();

    }

    public boolean isFirstLaunch() {
        firstLaunch = startupSharedPref.getBoolean("firstLaunch", false);
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putBoolean("firstLaunch", firstLaunch).apply();
    }

    public boolean isWarmStartStatus() {
        return warmStartStatus;
    }

    public void setWarmStartStatus(boolean warmStartStatus) {
        this.warmStartStatus = warmStartStatus;

        startupSharedPrefEditor = startupSharedPref.edit();
        startupSharedPrefEditor.putBoolean("warmStartStatus", warmStartStatus).apply();

    }
}