package com.appachhi.sdk.providers;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.monitor.startup.StartupTimeManager;


public class AppachhiInitializer extends ContentProvider {
    public static final String TAG = "AppachhiInitializer";

    public StartupTimeManager startupTimeManager;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private static final String DEVICE_ID_KEY = "device_id";
    private static final String SECURE_ID_KEY = "secure_id";


    public long cold_starttime;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            Appachhi.init((Application) getContext());
            Log.i(TAG, "Appachhi Instance Created");
        }
        return false;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        Log.d(TAG, "AppachhiInitializer attach");

        @SuppressLint("HardwareIds")
        String secureID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        sharedPreferences = context.getSharedPreferences("appachhi_pref", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.putString(SECURE_ID_KEY, secureID);
        editor.apply();



        startupTimeManager = new StartupTimeManager(context);
        cold_starttime = SystemClock.elapsedRealtime();
        startupTimeManager.setCold_starttime(cold_starttime);
        startupTimeManager.setFirstLaunch(true);
        if (info == null) {
            Log.d(TAG, "AppachhiInitializer ProviderInfo cannot be null");
        }
        if ("com.appachhi.sdk.initializer".equals(info.authority)) {
            throw new IllegalStateException("Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's build.gradle.");
        } else {
            super.attachInfo(context, info);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}