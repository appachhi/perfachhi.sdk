package com.appachhi.sdk.instrument.transition;

import android.util.Log;

import androidx.annotation.Nullable;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.BaseDataModule;

public class ScreenTransitionDataModule extends BaseDataModule<TransitionStat> {
    public static final String TAG = "ScrnTransDataModule";
    private ScreenTransitionManager screenTransitionManager;
    private TransitionStat transitionStat;
    private ScreenTransitionManager.OnTransitionListener onTransitionListener = new ScreenTransitionManager.OnTransitionListener() {
        @Override
        void onTransitionBegin(String name) {
        }

        @Override
        void onTransitionEnd(TransitionStat transitionStat) {
            ScreenTransitionDataModule.this.transitionStat = transitionStat;
            notifyObservers();
        }
    };

    ScreenTransitionDataModule(ScreenTransitionManager screenTransitionManager) {
        this.screenTransitionManager = screenTransitionManager;
        this.screenTransitionManager.registerListener(onTransitionListener);
    }

    @Nullable
    @Override
    protected TransitionStat getData() {
        return transitionStat;
    }

    @Override
    public void start() {
        if (Appachhi.DEBUG){
            Log.d(TAG, "start");
        }
        this.screenTransitionManager.registerListener(onTransitionListener);
    }

    @Override
    public void stop() {
        if (Appachhi.DEBUG){
            Log.d(TAG, "stop");
        }
        this.screenTransitionManager.unRegister(onTransitionListener);
    }

}
