package com.appachhi.sdk.monitor.network;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

/**
 * A Feature Module connecting {@link NetworkInfoDataModule} and {@link NetworkInfoDataObserver}
 */
public class NetworkFeatureModule extends FeatureModule<NetworkInfo> {
    private static final int DEFAULT_INTERVAL = 1500; // millis

    public NetworkFeatureModule(NetworkDao networkDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new NetworkInfoDataModule(DEFAULT_INTERVAL),
                new NetworkInfoViewDataObserver(),
                new NetworkInfoDataObserver(networkDao, dbExecutor, sessionManager));
    }
}
