package com.appachhi.sdk.monitor.memory;

import com.appachhi.sdk.monitor.common.FeatureModule;

/**
 *
 */
public class GCInfoFeatureModule extends FeatureModule<GCInfo> {
    public static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public GCInfoFeatureModule() {
        super(new GCInfoDataModule(), new GCInfoDataObserver());
    }
}
