package com.appachhi.sdk.monitor.logs;

import android.content.Context;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.LogsDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class LogsFeatureModule extends FeatureModule<LogsInfo> {
    public LogsFeatureModule(Context context, SessionManager sessionManager, LogsDao logsDao, ExecutorService databaseExecutor) {
        super(new LogsDataModule(context, sessionManager),
                new LogsDataObserver(sessionManager, databaseExecutor, logsDao));
    }
}
