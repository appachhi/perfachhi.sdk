package com.appachhi.sdk.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * This class decides when the modules should start observing for the data changes and when to stop
 * it
 * <p>
 * As of now,The Module will be publishes data once there is an active activity
 * i.e {@link Activity#onResume()} has been called and will continue to do so until all the
 * activity has been stopped i.e their {@link Activity#onPause} is called for all the activity
 */
public abstract class ActivityLifeCycleHandler implements Application.ActivityLifecycleCallbacks {
    private int currentlyRunningActivities = 0;
    private static final String TAG = "ActivityLifeCycle";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    /**
     * Increment the active activity count and calls startObserving as soon as
     * there is one active activity
     */
    private void incrementActivityCount() {
        Log.d(TAG, "incrementActivityCount");
        synchronized (this) {
            currentlyRunningActivities++;
            if (currentlyRunningActivities == 1) {
                startObserving();
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        incrementActivityCount();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        decrementActivityCount();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * Decrement the activity count and calls stopObserving as soon as there is no active activity
     */
    private void decrementActivityCount() {
        Log.d(TAG, "decrementActivityCount");
        synchronized (this) {
            currentlyRunningActivities--;
            if (currentlyRunningActivities == 0) {
                stopObserving();
            }
        }
    }

    /**
     * Method where {@link FeatureModule#start()} should be called.This is the right time when
     * module should start publishing result
     */
    protected abstract void startObserving();

    /**
     * Method where {@link FeatureModule#stop()} ()} should be called.This is the right time when
     * module should start publishing result
     */
    protected abstract void stopObserving();
}
