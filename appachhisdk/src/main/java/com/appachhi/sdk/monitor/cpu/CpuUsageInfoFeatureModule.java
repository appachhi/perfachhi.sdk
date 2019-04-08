package com.appachhi.sdk.monitor.cpu;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

/**
 * Feature Module for CPU Usage Information
 */
public class CpuUsageInfoFeatureModule extends FeatureModule<CpuUsageInfo> {
    private static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public CpuUsageInfoFeatureModule(CpuUsageDao cpuUsageDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        super(new CpuUsageInfoDataModule(DEFAULT_INTERVAL),
                new CpuUsageInfoViewDataObserver(),
                new CpuUsageInfoDataObserver(cpuUsageDao, databaseExecutor, sessionManager));
    }
}
