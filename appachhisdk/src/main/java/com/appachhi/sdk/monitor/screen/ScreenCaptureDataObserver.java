package com.appachhi.sdk.monitor.screen;

import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.DatabaseMapper;
import com.appachhi.sdk.database.dao.ScreenshotDao;
import com.appachhi.sdk.database.entity.ScreenshotEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

public class ScreenCaptureDataObserver implements DataObserver<String> {
    private static final String TAG = "ScreenCaptureObserver";
    private SessionManager sessionManager;
    private ScreenshotDao screenshotDao;
    private ExecutorService databaseExecutor;

    ScreenCaptureDataObserver(SessionManager sessionManager, ScreenshotDao screenshotDao, ExecutorService databaseExecutor) {
        this.sessionManager = sessionManager;
        this.screenshotDao = screenshotDao;
        this.databaseExecutor = databaseExecutor;
    }

    @Override
    public void onDataAvailable( final String filePath) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Session session = sessionManager.getCurrentSession();
                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    ScreenshotEntity screenshotEntity = DatabaseMapper.fromScreensShotFilePath(filePath, "image/jpeg", session.getId(), sessionTimeElapsed);
                    long result = screenshotDao.insertScreenshot(screenshotEntity);
                    if (result > -1) {
                        Log.d(TAG, "run: Screenshot path : " + filePath);
                        Log.i(TAG, "Screenshot Data saved");
                    }
                }
            }
        };
        databaseExecutor.submit(runnable);
    }
}
