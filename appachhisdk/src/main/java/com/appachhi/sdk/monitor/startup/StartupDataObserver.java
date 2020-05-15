package com.appachhi.sdk.monitor.startup;

import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.StartupDao;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.StartupEntity;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromStartupTimeInfotoStartupEntity;

public class StartupDataObserver implements DataObserver<StartupTimeInfo> {

    public String TAG = "StartupDataObserver";
    public long result;
    private StartupDao startupDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    public StartupDataObserver(StartupDao startupDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.startupDao = startupDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable(final StartupTimeInfo data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Create Session
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    //Create entity and pass data with session details.
                    StartupEntity startupEntity = fromStartupTimeInfotoStartupEntity(data, session.getId(), sessionTimeElapsed);
                    Log.d(TAG, "run: Data from StartupDataObserver : ColdStart : " + data.coldStartValue + " : Warm Start : " + data.warmStartValue + " : Result = " + result);

                    result = startupDao.insertStartupTimeinfo(startupEntity);

                    Log.d(TAG, "run: Result of insertStarupTimeInfo : " + result);
                }

            }
        };
        databaseExecutor.submit(runnable);

    }
}