package com.appachhi.sdk.monitor.framedrop;

import android.content.Context;
import android.util.Log;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class FrameDropFeatureModule extends FeatureModule<Long> {
    public FrameDropFeatureModule(Context context, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new FrameDropDataModule(context, 10), new FrameDropDataObserver(dbExecutor, sessionManager));
        Log.d("FrameDrop","Regsitered");

    }
}
