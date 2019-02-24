package com.appachhi.sdk.instrument.trace;

import android.os.SystemClock;
import android.util.Log;

public class MethodTrace {
    private static final String TAG = "Appachhi-Trace";
    private String className;
    private String methodName;
    private String traceName;
    private long startTime;
    private long duration = -1;

    public MethodTrace(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        startTime = SystemClock.elapsedRealtime();
        Log.i(TAG, this.startString());
    }

    public MethodTrace(String traceName) {
        this.traceName = traceName;
        startTime = SystemClock.elapsedRealtime();
        Log.i(TAG, this.startString());
    }

    public void stop() {
        duration = SystemClock.elapsedRealtime() - startTime;
        Log.i(TAG, this.stopString());
    }


    private String stopString() {
        final StringBuilder sb = new StringBuilder("MethodEnd{");
        if (this.className != null) {
            sb.append("className='").append(className).append('\'');
            sb.append(", methodName='").append(methodName).append('\'');
        }
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
        if (this.className != null) {
            sb.append("className='").append(className).append('\'');
            sb.append("methodName='").append(methodName).append('\'');
        }
        if (this.traceName != null) {
            sb.append("traceName='").append(traceName).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
