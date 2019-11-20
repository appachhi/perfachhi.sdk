package com.appachhi.sdk.monitor.memoryleak;


import shark.LeakTrace;

public class MemoryLeakInfo {
    private  String className;
    private LeakTrace leakTrace;

    MemoryLeakInfo(String className, LeakTrace leakTrace) {
        this.className = className;
        this.leakTrace = leakTrace;

    }


    public String getClassName() {
        return className;
    }

    public LeakTrace getLeakTrace() {
        return leakTrace;
    }
}
