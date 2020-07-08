package com.appachhi.sdk.monitor.battery.component;

import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.BatteryDataDao;
import com.appachhi.sdk.database.dao.StartupDao;
import com.appachhi.sdk.database.entity.BatteryEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.StartupEntity;
import com.appachhi.sdk.monitor.startup.StartupTimeInfo;
import com.appachhi.sdk.sync.SessionManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromBatteryDataInfotoBatteryEntity;

public class BatteryDataObserver implements DataObserver<BatteryDataInfo> {


    public String TAG = "BatteryDataObserver";
    public long result;
    private BatteryDataDao batteryDataDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    public BatteryDataObserver(BatteryDataDao batteryDataDao, ExecutorService databaseExecutor, SessionManager sessionManager) {
        this.batteryDataDao = batteryDataDao;
        this.databaseExecutor = databaseExecutor;
        this.sessionManager = sessionManager;

    }

    @Override
    public void onDataAvailable(final BatteryDataInfo data) {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Session session = sessionManager.getCurrentSession();

                if (session != null) {
                    long sessionTimeElapsed = new Date().getTime() - session.getStartTime();
                    //Create entity and pass data with session details.
                    BatteryEntity batteryEntity = fromBatteryDataInfotoBatteryEntity(data, session.getId(), sessionTimeElapsed);

                    result = batteryDataDao.insertBatteryDataInfo(batteryEntity);

                }


            }
        };
        databaseExecutor.submit(runnable);

    }
}
