package com.appachhi.sdk.providers;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appachhi.sdk.Appachhi;

@Keep
public class AppachhiInitializer extends ContentProvider {
    public static final String TAG = "AppachhiInitializer";

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
        if (info == null){
            Log.d(TAG, "AppachhiInitializer ProviderInfo cannot be null");
        }
        if ("com.appachhi.sdk.initializer".equals(info.authority)) {
            throw new IllegalStateException("Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's build.gradle.");
        } else {
            super.attachInfo(context, info);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
