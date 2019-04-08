package com.appachhi.sdk.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

public abstract class BaseEntity {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "session_id")
    private String sessionId;

    BaseEntity(String sessionId) {
        this();
        this.sessionId = sessionId;
    }

    BaseEntity() {
        id = UUID.randomUUID().toString();
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
}
