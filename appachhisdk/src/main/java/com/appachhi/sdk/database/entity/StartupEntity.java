package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

public class StartupEntity extends BaseEntity {

    @SerializedName("coldStartResult")
    private long coldStartResult;

    @SerializedName("warmStartResult")
    private long warmStartResult;

    public StartupEntity(long coldStartResult, long warmStartResult, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.coldStartResult = coldStartResult;
        this.warmStartResult = warmStartResult;

    }

    public StartupEntity() {

    }

    public long getColdStartResult() {
        return coldStartResult;
    }

    public void setColdStartResult(long coldStartResult) {
        this.coldStartResult = coldStartResult;
    }

    public long getWarmStartResult() {
        return warmStartResult;
    }

    public void setWarmStartResult(long warmStartResult) {
        this.warmStartResult = warmStartResult;
    }
}