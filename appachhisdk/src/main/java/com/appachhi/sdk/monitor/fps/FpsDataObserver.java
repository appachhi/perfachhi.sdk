package com.appachhi.sdk.monitor.fps;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

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
    public void onDataAvailable(@NonNull final Double data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null && session.getId() != null) {
                    FpsEntity fpsEntity = fromDoubleToFpsEntity(data, session.getId());
                    long result = fpsDao.insertFps(fpsEntity);
                    if (result > -1) {
                        Log.i(TAG, "Fps Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
