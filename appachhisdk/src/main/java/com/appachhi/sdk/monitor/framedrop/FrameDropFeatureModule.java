package com.appachhi.sdk.monitor.framedrop;

import android.content.Context;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.FrameDropDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class FrameDropFeatureModule extends FeatureModule<Long> {
    public FrameDropFeatureModule(Context context, ExecutorService dbExecutor, SessionManager sessionManager, FrameDropDao frameDropDao) {
        super(new FrameDropDataModule(context, 10), new FrameDropDataObserver(dbExecutor, sessionManager, frameDropDao));
    }
}
