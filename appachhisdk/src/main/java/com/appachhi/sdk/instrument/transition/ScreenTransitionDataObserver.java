package com.appachhi.sdk.instrument.transition;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;

import java.util.Locale;

public class ScreenTransitionDataObserver implements DataObserver<TransitionStat> {
    private static final String TAG = "Appachhi-ScrTransition";

    @Override
    public void onDataAvailable(@NonNull TransitionStat data) {
        try {
            long duration = data.transitionDuration();
            Log.d(TAG, String.format(Locale.ENGLISH, "%s took %d milliseconds to complete transition", data.getScreenName(), duration));
        } catch (IllegalStateException e) {
            Log.e("Appachhi", "Failed to log transition details", e);
        }
    }
}
