package com.appachhi.sdk.providers;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.monitor.startup.StartupTimeManager;


public class AppachhiInitializer extends ContentProvider {
    public static final String TAG = "AppachhiInitializer";

    public StartupTimeManager startupTimeManager;

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