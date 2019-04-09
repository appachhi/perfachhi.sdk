package com.appachhi.sdk.monitor.memory;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromMemoryInfoToMemoryEntity;


/**
 * {@link MemoryInfo} Data Observer logging the information on to the logcat
 */
public class MemoryInfoDataObserver implements DataObserver<MemoryInfo> {
    private static final String TAG = "Appachhi-Memory";


    private MemoryDao memoryDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    MemoryInfoDataObserver(MemoryDao memoryDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.memoryDao = memoryDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    @MainThread
    @Override
    public void onDataAvailable(@NonNull final MemoryInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null && session.getId() != null) {
                    MemoryEntity memoryEntity = fromMemoryInfoToMemoryEntity(data, session.getId());
                    long result = memoryDao.insertMemoryUsage(memoryEntity);
                    if (result > -1) {
                        Log.i(TAG, "Memory Usage Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
