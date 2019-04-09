package com.appachhi.sdk.instrument.trace;

import android.os.SystemClock;

public class MethodTrace {
    private static final String TAG = "Appachhi-Trace";
    private String traceName;
    private long startTime;
    private long duration = -1;

    private MethodTraceSavingManager methodTraceSavingManager;

    public MethodTrace(String traceName, MethodTraceSavingManager methodTraceSavingManager) {
        this.traceName = traceName;
        this.methodTraceSavingManager = methodTraceSavingManager;
        startTime = SystemClock.elapsedRealtime();
    }

    public String getTraceName() {
        return traceName;
    }

    public long getDuration() {
        return duration;
    }

    public void stop() {
        duration = SystemClock.elapsedRealtime() - startTime;
        methodTraceSavingManager.save(this);
    }


    private String stopString() {
        final StringBuilder sb = new StringBuilder("MethodEnd{");
        if (this.traceName != null) {
            sb.append("traceName='").append(traceName).append('\'');
        }
        if (duration != -1) {
            sb.append(", duration=").append(duration);
        }
        sb.append('}');
        return sb.toString();
    }

    private String startString() {
        final StringBuilder sb = new StringBuilder("MethodStart{");
        if (this.traceName != null) {
            sb.append("traceName='").append(traceName).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
