package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

public class StartupEntity extends BaseEntity {

    @SerializedName("coldStartValue")
    private long coldStartValue;

    @SerializedName("warmStartValue")
    private long warmStartValue;

    public StartupEntity(long coldStartValue, long warmStartValue, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.coldStartValue = coldStartValue;
        this.warmStartValue = warmStartValue;

    }

    public StartupEntity() {

    }

    public long getColdStartValue() {
        return coldStartValue;
    }

    public void setColdStartValue(long coldStartValue) {
        this.coldStartValue = coldStartValue;
    }

    public long getWarmStartValue() {
        return warmStartValue;
    }

    public void setWarmStartValue(long warmStartValue) {
        this.warmStartValue = warmStartValue;
    }
}