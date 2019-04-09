package com.appachhi.sdk.monitor.cpu;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromCpuUsageInfoToCpuUsageEntity;

/**
 * {@link CpuUsageInfo} Data Observer logging the CPU Usage related information on to the logcat
 */
class CpuUsageInfoDataObserver implements DataObserver<CpuUsageInfo> {
    private static final String TAG = "Appachhi-CPU-Usage";
    private CpuUsageDao cpuUsageDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    CpuUsageInfoDataObserver(CpuUsageDao cpuUsageDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.cpuUsageDao = cpuUsageDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable(@NonNull CpuUsageInfo data) {
        saveToDb(data);
    }

    private void saveToDb(@NonNull final CpuUsageInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    CpuUsageEntity cpuUsageEntity = fromCpuUsageInfoToCpuUsageEntity(data, session.getId(), sessionTimeElapsed);
                    long result = cpuUsageDao.insertCpuUsage(cpuUsageEntity);
                    if (result > -1) {
                        Log.i(TAG, "CPU Usage Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
