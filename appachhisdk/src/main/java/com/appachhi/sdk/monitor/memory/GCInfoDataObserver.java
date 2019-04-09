package com.appachhi.sdk.monitor.memory;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataModule;
import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromGCInfoToGCEntity;

/**
 * {@link GCInfo} Data Observer logging the GC related information on to the logcat
 */
public class GCInfoDataObserver implements DataObserver<GCInfo> {
    private static final String TAG = "Appachhi-GC";

    private GCDao gcDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    GCInfoDataObserver(GCDao gcDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.gcDao = gcDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    /**
     * Logs {@link GCInfo} as Json String
     *
     * @param data Data Published by {@link DataModule}
     */
    @MainThread
    @Override
    public void onDataAvailable(@NonNull final GCInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    GCEntity gcEntity = fromGCInfoToGCEntity(data, session.getId(),sessionTimeElapsed);
                    long result = gcDao.insetGcRunInfo(gcEntity);
                    if (result > -1) {
                        Log.i(TAG, "GC Run Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
