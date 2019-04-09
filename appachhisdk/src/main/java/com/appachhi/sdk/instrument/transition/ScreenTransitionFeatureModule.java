package com.appachhi.sdk.instrument.transition;

import com.appachhi.sdk.FeatureModule;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.sync.SessionManager;

import java.util.concurrent.ExecutorService;

public class ScreenTransitionFeatureModule extends FeatureModule<TransitionStat> {
    public ScreenTransitionFeatureModule(ScreenTransitionManager screenTransitionManager,
                                         ScreenTransitionDao screenTransitionDao,
                                         ExecutorService dbExecutor, SessionManager sessionManager) {
        super(new ScreenTransitionDataModule(screenTransitionManager),
                new ScreenTransitionViewDataObserver(),
                new ScreenTransitionDataObserver(screenTransitionDao,dbExecutor,sessionManager));
    }
}
