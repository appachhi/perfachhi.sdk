package com.appachhi.sdk.memory;

import com.appachhi.sdk.common.FeatureModule;

/**
 *
 */
public class GCInfoFeatureModule extends FeatureModule<GCInfo> {
    public static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public GCInfoFeatureModule() {
        super(new GCInfoDataModule(), new GCInfoDataObserver());
    }
}
