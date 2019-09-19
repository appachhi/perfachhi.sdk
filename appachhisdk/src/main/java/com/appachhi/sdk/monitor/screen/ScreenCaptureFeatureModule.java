package com.appachhi.sdk.monitor.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.sync.SessionManager;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureFeatureModule extends FeatureModule<String> {
    private ScreenCaptureDataModule dataModule;
    private boolean screenShotEnabled;

    public ScreenCaptureFeatureModule(Context context, SessionManager sessionManager) {
        super(null, new ScreenCaptureDataObserver());
        dataModule = new ScreenCaptureDataModule(context, sessionManager, 1000);
        setPublisher(dataModule);
    }

    public void handleMediaProjectionResult(int resultCode, Intent result) {
        dataModule.handleMediaProjectionResult(resultCode, result);
    }

    public void toggleProjection(Activity activity, boolean enabled) {
        screenShotEnabled = enabled;
        if (screenShotEnabled) {
            dataModule.createMediaProjection(activity);
        } else {
            dataModule.stopProjection();
        }
    }

    public boolean isScreenShotEnabled() {
        return screenShotEnabled;
    }
}
