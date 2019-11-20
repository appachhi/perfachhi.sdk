package com.appachhi.sdk.database.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public abstract class BaseEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("sessionId")
    @ColumnInfo(name = "session_id")
    private String sessionId;

    @ColumnInfo(name = "execution_time")
    @SerializedName("executionTime")
    private long executionTime;

    @ColumnInfo(name = "session_time")
    @SerializedName("sessionTime")
    private long sessionTime;

    /**
     * Sync Status 0 means unsynced
     * Sync Status 1 means synced
     */
    @ColumnInfo(name = "syncStatus")
    private transient int syncStatus;
    BaseEntity(String sessionId, long sessionTime) {
        this();
        this.sessionId = sessionId;
        this.sessionTime = sessionTime;
    }

    BaseEntity() {
        id = UUID.randomUUID().toString();
        executionTime = new Date().getTime();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
