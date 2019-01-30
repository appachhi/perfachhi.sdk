package com.appachhi.sdk;

import android.app.Application;
import android.support.annotation.NonNull;

import com.appachhi.sdk.common.ActivityLifeCycleHandler;
import com.appachhi.sdk.common.FeatureModule;
import com.appachhi.sdk.memory.GCInfoFeatureModule;
import com.appachhi.sdk.memory.MemoryInfoFeatureModule;
import com.appachhi.sdk.network.NetworkFeatureModule;

import java.util.Arrays;
import java.util.List;

public class Appachhi {

    private List<FeatureModule> featureModules;
    @SuppressWarnings("FieldCanBeLocal")
    private ActivityLifeCycleHandler activityLifeCycleHandler = new ActivityLifeCycleHandler() {
        @Override
        protected void startObserving() {
            for (FeatureModule featureModule : featureModules) {
                featureModule.start();
            }
        }

        @Override
        protected void stopObserving() {
            for (FeatureModule featureModule : featureModules) {
                featureModule.stop();
            }
        }
    };

    private Appachhi(Application application, List<FeatureModule> featureModules) {
        application.registerActivityLifecycleCallbacks(activityLifeCycleHandler);
        this.featureModules = featureModules;
    }

    public static Appachhi init(@NonNull Application application) {
        return new Appachhi(application, Arrays.<FeatureModule>asList(
                new MemoryInfoFeatureModule(application),
                new NetworkFeatureModule(),
                new GCInfoFeatureModule()));
    }
}
