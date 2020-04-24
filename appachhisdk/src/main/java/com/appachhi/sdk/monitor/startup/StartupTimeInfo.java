package com.appachhi.sdk.monitor.startup;

public class StartupTimeInfo {

    public long coldStartValue;
    public long warmStartValue;

    public StartupTimeInfo(long coldStartValue, long warmStartValue) {
        this.coldStartValue = coldStartValue;
        this.warmStartValue = warmStartValue;
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