package com.appachhi.sdk.monitor.network;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromNetworkUsageInfoToNetworkUsageEntity;

/**
 * {@link NetworkInfo} data observer which logs the information on to the logcat
 */
public class NetworkInfoDataObserver implements DataObserver<NetworkInfo> {
    private static final String TAG = "Appachhi-Network";

    private NetworkDao networkDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    NetworkInfoDataObserver(NetworkDao networkDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.networkDao = networkDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    /**
     * Logs the {@link NetworkInfo} on to the logcat as Json string
     *
     * @param data published
     */
    @MainThread
    @Override
    public void onDataAvailable(@NonNull final NetworkInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null && session.getId() != null) {
                    NetworkUsageEntity networkUsageEntity = fromNetworkUsageInfoToNetworkUsageEntity(data, session.getId());
                    long result = networkDao.insertNetworkUsage(networkUsageEntity);
                    if (result > -1) {
                        Log.i(TAG, "Network Usage Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
