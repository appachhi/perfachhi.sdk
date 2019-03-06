package com.appachhi.sdk.monitor.fps;

import com.appachhi.sdk.FeatureModule;

public class FpsFeatureModule extends FeatureModule<Double> {
    private static final int INTERVAL = 1000; // 1000ms

    public FpsFeatureModule() {
        super(new FpsDataModule(INTERVAL), new FpsViewDataObserver(), new FpsDataObserver());
    }
}
