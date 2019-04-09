package com.appachhi.sdk.monitor.memory;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

/**
 * Feature Module for GC Information
 */
public class GCInfoFeatureModule extends FeatureModule<GCInfo> {
    public static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public GCInfoFeatureModule(GCDao gcDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new GCInfoDataModule(), new GCInfoDataObserver(gcDao, dbExecutor, sessionManager));
    }
}
