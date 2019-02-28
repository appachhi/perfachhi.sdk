package com.appachhi.sdk.monitor.network;

import com.appachhi.sdk.FeatureModule;

/**
 * A Feature Module connecting {@link NetworkInfoDataModule} and {@link NetworkInfoDataObserver}
 */
public class NetworkFeatureModule extends FeatureModule<NetworkInfo> {
    private static final int DEFAULT_INTERVAL = 1500; // millis

    public NetworkFeatureModule() {
        super(new NetworkInfoDataModule(DEFAULT_INTERVAL), new NetworkInfoDataObserver());
    }
}
