package com.appachhi.sdk.monitor.battery.component;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.BatteryDataDao;
import com.appachhi.sdk.database.dao.StartupDao;
import com.appachhi.sdk.monitor.startup.StartupDataModule;
import com.appachhi.sdk.monitor.startup.StartupDataObserver;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class BatteryDataFeatureModule extends FeatureModule<BatteryDataInfo> {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryDataFeatureModule(Context context, BatteryDataDao startupDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new BatteryDataModule(context),
                new BatteryDataObserver(startupDao, dbExecutor, sessionManager));
    }
}
