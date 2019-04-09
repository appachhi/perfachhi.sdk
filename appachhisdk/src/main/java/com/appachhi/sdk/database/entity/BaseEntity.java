package com.appachhi.sdk.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

public abstract class BaseEntity {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "session_id")
    private String sessionId;
    @ColumnInfo(name = "execution_time")
    private long executionTime;
    @ColumnInfo(name = "session_time")
    private long sessionTime;

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
}
