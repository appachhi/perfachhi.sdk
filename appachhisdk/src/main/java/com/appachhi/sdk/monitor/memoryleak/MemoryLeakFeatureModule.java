package com.appachhi.sdk.monitor.memoryleak;

import android.app.Application;

import com.appachhi.sdk.FeatureModule;

import java.util.List;

public class MemoryLeakFeatureModule extends FeatureModule<List<MemoryLeakInfo>> {
    public MemoryLeakFeatureModule(Application application) {
        super(new MemoryLeakDataModule(application),
                new MemoryLeakInfoViewDataObserver(application), new MemoryLeakDataObserver());
    }
}
