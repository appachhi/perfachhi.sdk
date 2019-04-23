package com.appachhi.sdk.instrument.network.internal;

import android.util.Log;

import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromInterHttpMetricToApiCallEntity;

public class HttpMetricSavingManager {
    public static final String TAG = "HttpMetricSavManager";
    private APICallDao apiCallDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    public HttpMetricSavingManager(APICallDao apiCallDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.apiCallDao = apiCallDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    void save(final HttpMetric data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    APICallEntity apiCallEntity = fromInterHttpMetricToApiCallEntity(data, session.getId(),sessionTimeElapsed);
                    long result = apiCallDao.insertApiCall(apiCallEntity);
                    if (result > -1) {
                        Log.i(TAG, "Api Call Trace saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
