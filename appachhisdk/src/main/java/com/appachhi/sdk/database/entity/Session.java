package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "session")
public class Session {
    private static String VERISON_NAME = null;
    private static long VERISON_CODE = -1L;
    private static String PACKAGE_NAME = null;
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;
    @ColumnInfo(name = "start_time")
    @SerializedName("startTime")
    private long startTime;

    /**
     * Sync Status 0 means unsynced
     * Sync Status 1 means synced
     */
    @ColumnInfo(name = "syncStatus")
    private transient int syncStatus;

    @ColumnInfo(name = "version_code")
    private long versionCode;
    @ColumnInfo(name = "version_name")
    private String versionName;

    @ColumnInfo(name = "package_name")
    private String packageName;

    @Ignore
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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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
                VERISON_CODE = pInfo.getLongVersionCode();
                PACKAGE_NAME = context.getPackageName();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Session(VERISON_NAME, VERISON_CODE, PACKAGE_NAME);
    }

}
