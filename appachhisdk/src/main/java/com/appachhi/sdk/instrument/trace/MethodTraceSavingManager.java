package com.appachhi.sdk.instrument.trace;

import android.util.Log;

import com.appachhi.sdk.database.dao.MethodTraceDao;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromMethodTraceToMethodTraceEntity;

public class MethodTraceSavingManager {
    public static final String TAG = "MethodTrcSavingManager";
    private MethodTraceDao methodTraceDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    public MethodTraceSavingManager(MethodTraceDao methodTraceDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.methodTraceDao = methodTraceDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    void save(final MethodTrace data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null && session.getId() != null) {
                    MethodTraceEntity methodTraceEntity = fromMethodTraceToMethodTraceEntity(data, session.getId());
                    long result = methodTraceDao.addTrace(methodTraceEntity);
                    if (result > -1) {
                        Log.i(TAG, "Method Trace saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
