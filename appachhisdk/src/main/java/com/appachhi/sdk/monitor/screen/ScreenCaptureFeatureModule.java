package com.appachhi.sdk.monitor.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.ScreenshotDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class ScreenCaptureFeatureModule extends FeatureModule<String> {
    private boolean screenShotEnabled;

    public ScreenCaptureFeatureModule(Context context, SessionManager sessionManager, ScreenshotDao screenshotDao, ExecutorService databaseExecutor) {
        super(new ScreenCaptureDataModule(context, sessionManager, 1000),
                new ScreenCaptureDataObserver(sessionManager, screenshotDao, databaseExecutor));
    }

    public void handleMediaProjectionResult(int resultCode, Intent result) {
        ScreenCaptureDataModule publisher = (ScreenCaptureDataModule) getPublisher();
        publisher.handleMediaProjectionResult(resultCode, result);
    }

    public void toggleProjection(Activity activity, boolean enabled) {
        Log.d("ScreenCaptureFM", "toggleProjection: triggered + " + enabled);
        ScreenCaptureDataModule publisher = (ScreenCaptureDataModule) getPublisher();
        screenShotEnabled = enabled;
        if (screenShotEnabled) {
            Log.d("ScreenCaptureFM", "toggleProjection: if screenshotEnable is " + enabled);
            publisher.createMediaProjection(activity);
        } else {
            Log.d("ScreenCaptureFM", "toggleProjection: else screenshotEnable is " + enabled);

            publisher.stopProjection();
        }
    }

    public boolean isScreenShotEnabled() {
        return screenShotEnabled;
    }
}
