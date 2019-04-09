package com.appachhi.sdk.monitor.memoryleak;

import android.app.Application;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class MemoryLeakFeatureModule extends FeatureModule<List<MemoryLeakInfo>> {
    public MemoryLeakFeatureModule(Application application, MemoryLeakDao memoryLeakDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new MemoryLeakDataModule(application),
                new MemoryLeakInfoViewDataObserver(application),
                new MemoryLeakDataObserver(memoryLeakDao, dbExecutor, sessionManager));
    }
}
