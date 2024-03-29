package com.appachhi.sdk.monitor.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.appachhi.sdk.BaseDataModule;

/**
 * A Data Module Providing information about the Phone Memory as well as the current process memory
 */
class MemoryInfoDataModule extends BaseDataModule<MemoryInfo> implements Runnable {
    // Main thread handler
    private Handler handler = new Handler(Looper.getMainLooper());
    // Interval for polling memory information
    private long interval;
    private ActivityManager am;

    private MemoryInfo data;

    MemoryInfoDataModule(Context context, long interval) {
        this.interval = interval;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }


    @Override
    protected MemoryInfo getData() {
        return data;
    }

    @Override
    public void start() {
        handler.post(this);
    }

    @Override
    public void stop() {
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        // Polls the system memory information
        ActivityManager.MemoryInfo systemMemInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(systemMemInfo);
        // Polls the current process memory information
        Debug.MemoryInfo processMemInfo = am.getProcessMemoryInfo(new int[]{Process.myPid()})[0];
        data = new MemoryInfo(processMemInfo, systemMemInfo);
        notifyObservers();
        handler.postDelayed(this, interval);
    }
}
