package com.appachhi.sdk.monitor.fps;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromDoubleToFpsEntity;

public class FpsDataObserver implements DataObserver<Double> {

    private static final String TAG = "Appachhi-FPS";

    private FpsDao fpsDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    FpsDataObserver(FpsDao fpsDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.fpsDao = fpsDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable( final Double data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    FpsEntity fpsEntity = fromDoubleToFpsEntity(data, session.getId(),sessionTimeElapsed);
                    long result = fpsDao.insertFps(fpsEntity);
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
