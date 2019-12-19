package com.appachhi.sdk.monitor.framedrop;

import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class FrameDropDataObserver implements DataObserver<Long> {
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    FrameDropDataObserver(ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable( Long data) {
        Log.d("FrameDropDataObserver", String.format("FrameDropped  %d", data));
    }
}
