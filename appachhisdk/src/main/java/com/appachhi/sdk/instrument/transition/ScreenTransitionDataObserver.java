package com.appachhi.sdk.instrument.transition;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

import static com.appachhi.sdk.database.DatabaseMapper.fromTransitionStatToTransitionStatEntity;

public class ScreenTransitionDataObserver implements DataObserver<TransitionStat> {
    private static final String TAG = "Appachhi-ScrTransition";

    private ScreenTransitionDao screenTransitionDao;
    private ExecutorService databaseExecutor;
    private SessionManager sessionManager;

    ScreenTransitionDataObserver(ScreenTransitionDao screenTransitionDao, ExecutorService dbExecutor, SessionManager sessionManager) {
        this.screenTransitionDao = screenTransitionDao;
        this.databaseExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onDataAvailable(@NonNull final TransitionStat data) {
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Session session = sessionManager.getCurrentSession();
                    if (session != null && session.getId() != null) {
                        TransitionStatEntity transitionStatEntity = fromTransitionStatToTransitionStatEntity(data, session.getId());
                        long result = screenTransitionDao.insertScreenTranData(transitionStatEntity);
                        if (result > -1) {
                            Log.i(TAG, "Transition stat saved");
                        }
                    }
                }
            };
            databaseExecutor.submit(runnable);
        } catch (IllegalStateException e) {
            Log.e("Appachhi", "Failed to log transition details", e);
        }
    }
}
