package com.appachhi.sdk.database.entity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Session {
    private static String VERISON_NAME = null;
    private static long VERISON_CODE = -1L;
    private static String PACKAGE_NAME = null;

    @SerializedName("id")
    private String id;
    @SerializedName("startTime")
    private long startTime;

    /**
     * Sync Status 0 means unsynced
     * Sync Status 1 means synced
     */
    private transient int syncStatus;

    private long versionCode;
    private String versionName;
    private String packageName;

    public Session(String versionName, long versionCode, String packageName) {
        this();
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.packageName = packageName;
    }

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.startTime = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public static Session create(Context context) {
        if (VERISON_CODE == -1 && VERISON_NAME == null && PACKAGE_NAME == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                VERISON_NAME = pInfo.versionName;
                VERISON_CODE = pInfo.versionCode;
                PACKAGE_NAME = context.getPackageName();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Session(VERISON_NAME, VERISON_CODE, PACKAGE_NAME);
    }
}
