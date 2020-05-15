package com.appachhi.sdk.monitor.startup;

import android.content.Context;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.StartupDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class StartupFeatureModule extends FeatureModule<StartupTimeInfo> {
    //Create Dao
    public StartupFeatureModule(Context context, StartupDao startupDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new StartupDataModule(context), new StartupDataObserver(startupDao, dbExecutor, sessionManager));
    }
}