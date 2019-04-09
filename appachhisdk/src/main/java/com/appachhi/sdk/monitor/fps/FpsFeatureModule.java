package com.appachhi.sdk.monitor.fps;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class FpsFeatureModule extends FeatureModule<Double> {
    private static final int INTERVAL = 500; // 1000ms

    public FpsFeatureModule(FpsDao fpsDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new FpsDataModule(INTERVAL), new FpsViewDataObserver(),
                new FpsDataObserver(fpsDao, dbExecutor, sessionManager));
    }
}
