package com.appachhi.sdk.monitor.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.ScreenshotDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        ScreenCaptureDataModule publisher = (ScreenCaptureDataModule) getPublisher();
        screenShotEnabled = enabled;
        if (screenShotEnabled) {
            publisher.createMediaProjection(activity);
        } else {
            publisher.stopProjection();
        }
    }

    public boolean isScreenShotEnabled() {
        return screenShotEnabled;
    }
}
