package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "session")
public class Session {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @ColumnInfo(name = "start_time")
    @SerializedName("startTime")
    private long startTime;

    /**
     * Sync Status 0 means unsynced
     * Sync Status 1 means synced
     */
    @ColumnInfo(name = "syncStatus")
    private transient int syncStatus;

    @Ignore
    public Session(String manufacturer, String model) {
        this();
        this.manufacturer = manufacturer;
        this.model = model;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public static Session create() {
        return new Session(Build.BRAND, Build.MODEL);
    }

}
