package com.appachhi.sdk.monitor.framedrop;

import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.FrameDropDao;
import com.appachhi.sdk.database.entity.FrameDropEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromLongToFrameDropEntity;

public class FrameDropDataObserver implements DataObserver<Long> {
    private static final String TAG = "Appachhi-FrameDrop";
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;
    private FrameDropDao frameDropDao;


    FrameDropDataObserver(ExecutorService databaseExecutor, SessionManager sessionManager, FrameDropDao frameDropDao) {
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;
        this.frameDropDao = frameDropDao;
    }

    @Override
    public void onDataAvailable(final Long data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    FrameDropEntity frameDropEntity = fromLongToFrameDropEntity(data, session.getId(), sessionTimeElapsed);
                    long result = frameDropDao.insertFrameDrop(frameDropEntity);
                    if (result > -1) {
                        Log.i(TAG, "Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
