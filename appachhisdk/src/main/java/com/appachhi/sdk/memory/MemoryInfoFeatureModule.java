package com.appachhi.sdk.memory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appachhi.sdk.common.FeatureModule;

/**
 * A Feature Module connecting {@link MemoryInfoDataModule} and {@link MemoryInfoDataObserver}
 */
public class MemoryInfoFeatureModule extends FeatureModule<MemoryInfo> {
    private static final int DEFAULT_INTERVAL = 1500; //1500 ms

    public MemoryInfoFeatureModule(@NonNull Context context) {
        super(new MemoryInfoDataModule(context, DEFAULT_INTERVAL), new MemoryInfoDataObserver());
    }
}