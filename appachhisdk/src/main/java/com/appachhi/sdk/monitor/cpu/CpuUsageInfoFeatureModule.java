package com.appachhi.sdk.monitor.cpu;

import com.appachhi.sdk.monitor.common.FeatureModule;

/**
 * Feature Module for CPU Usage Information
 */
public class CpuUsageInfoFeatureModule extends FeatureModule<CpuUsageInfo> {
    private static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public CpuUsageInfoFeatureModule() {
        super(new CpuUsageInfoDataModule(DEFAULT_INTERVAL), new CpuUsageInfoDataObserver());
    }
}
