package com.appachhi.sdk.instrument.transition;

import com.appachhi.sdk.FeatureModule;

public class ScreenTransitionFeatureModule extends FeatureModule<TransitionStat> {
    public ScreenTransitionFeatureModule(ScreenTransitionManager screenTransitionManager) {
        super(new ScreenTransitionDataModule(screenTransitionManager),
                new ScreenTransitionViewDataObserver(),
                new ScreenTransitionDataObserver());
    }
}
