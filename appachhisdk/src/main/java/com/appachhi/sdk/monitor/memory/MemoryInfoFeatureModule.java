package com.appachhi.sdk.monitor.memory;

import android.content.Context;

import androidx.annotation.NonNull;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

/**
 * A Feature Module connecting {@link MemoryInfoDataModule} and {@link MemoryInfoDataObserver}
 */
public class MemoryInfoFeatureModule extends FeatureModule<MemoryInfo> {
    private static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public MemoryInfoFeatureModule(@NonNull Context context, MemoryDao memoryDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new MemoryInfoDataModule(context, DEFAULT_INTERVAL),
                new MemoryInfoViewDataObserver(),
                new MemoryInfoDataObserver(memoryDao,dbExecutor,sessionManager));
    }
}