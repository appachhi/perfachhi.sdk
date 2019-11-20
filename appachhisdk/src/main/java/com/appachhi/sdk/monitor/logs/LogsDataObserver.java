package com.appachhi.sdk.monitor.logs;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.DatabaseMapper;
import com.appachhi.sdk.database.dao.LogsDao;
import com.appachhi.sdk.database.entity.LogsEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

public class LogsDataObserver implements DataObserver<LogsInfo> {
    public static final String TAG = "LogsDataObserver";
    private SessionManager sessionManager;
    private ExecutorService databaseExecutor;
    private LogsDao logsDao;

    public LogsDataObserver(SessionManager sessionManager, ExecutorService databaseExecutor, LogsDao logsDao) {
        this.sessionManager = sessionManager;
        this.databaseExecutor = databaseExecutor;
        this.logsDao = logsDao;
    }

    @Override
    public void onDataAvailable(@NonNull final LogsInfo logsInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    LogsEntity logsEntity = DatabaseMapper
                            .fromLogsInfo(
                                    logsInfo.getLogFilePath(),
                                    session.getId(),
                                    sessionTimeElapsed);
                    long result = logsDao.insertLogs(logsEntity);
                    if (result > -1) {
                        Log.i(TAG, "Logs Data saved :" + logsInfo.getLogFilePath());
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
