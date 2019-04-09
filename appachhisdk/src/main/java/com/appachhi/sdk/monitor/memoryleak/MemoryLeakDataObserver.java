package com.appachhi.sdk.monitor.memoryleak;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromMemoryLeakInfoTOMemoryLeakEntity;

public class MemoryLeakDataObserver implements DataObserver<List<MemoryLeakInfo>> {
    private static final String TAG = "Appachhi-MemoryLeak";

    private MemoryLeakDao memoryLeakDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    MemoryLeakDataObserver(MemoryLeakDao memoryLeakDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.memoryLeakDao = memoryLeakDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable(@NonNull final List<MemoryLeakInfo> memoryLeakInfos) {
        if (memoryLeakInfos.size() > 0) {
            final MemoryLeakInfo data = memoryLeakInfos.get(memoryLeakInfos.size() - 1);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Session session = sessionManager.getCurrentSession();
                    if (session != null && session.getId() != null) {
                        MemoryLeakEntity memoryLeakEntity = fromMemoryLeakInfoTOMemoryLeakEntity(data, session.getId());
                        long result = memoryLeakDao.insertMemoryLeak(memoryLeakEntity);
                        if (result > -1) {
                            Log.i(TAG, "Memory Leak Data saved");
                        }
                    }
                }
            };
            databaseExecutor.submit(runnable);
        }
    }
}
