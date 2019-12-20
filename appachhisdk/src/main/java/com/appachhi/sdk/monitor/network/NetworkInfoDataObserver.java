package com.appachhi.sdk.monitor.network;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private Map<String, NetworkInfo> initialSessionNetworkInfo;

    NetworkInfoDataObserver(NetworkDao networkDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.networkDao = networkDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        this.initialSessionNetworkInfo = new HashMap<>();
    }

    /**
     * Logs the {@link NetworkInfo} on to the logcat as Json string
     *
     * @param data published
     */

    @Override
    public void onDataAvailable( final NetworkInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    NetworkInfo finalNetworkData;
                    if (initialSessionNetworkInfo.containsKey(session.getId())) {
                        NetworkInfo initialNetworkInfo = initialSessionNetworkInfo.get(session.getId());
                        finalNetworkData = data.subtract(initialNetworkInfo);
                    } else {
                        initialSessionNetworkInfo.put(session.getId(), data);
                        finalNetworkData = data;
                    }
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    NetworkUsageEntity networkUsageEntity = fromNetworkUsageInfoToNetworkUsageEntity(finalNetworkData, session.getId(), sessionTimeElapsed);
                    long result = networkDao.insertNetworkUsage(networkUsageEntity);
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
