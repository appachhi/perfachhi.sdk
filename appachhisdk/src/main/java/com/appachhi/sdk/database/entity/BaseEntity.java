package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public abstract class BaseEntity {
    @SerializedName("id")
    private String id;

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("executionTime")
    private long executionTime;

    @SerializedName("sessionTime")
    private long sessionTime;

    /**
     * Sync Status 0 means unsynced
     * Sync Status 1 means synced
     */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
