package com.appachhi.sdk.instrument.transition;

import android.app.Activity;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;
import java.util.WeakHashMap;

/**
 * Class responsible for handling the screen transition tracing.This class can trace the screen transition for a given
 * activity as well as generic screen name. Precession of the time duration is up to millis i.e all time information
 * provided by this API will be in {@link java.util.concurrent.TimeUnit#MILLISECONDS}
 */
final public class ScreenTransitionManager {
    private static ScreenTransitionManager instance;

    private ScreenTransitionManager() {
    }

    public static ScreenTransitionManager getInstance() {
        synchronized (ScreenTransitionManager.class) {
            if (instance == null) {
                instance = new ScreenTransitionManager();
            }
            return instance;
        }
    }

    private WeakHashMap<String, TransitionStat> screenTransitionStats = new WeakHashMap<>();
    private static String tag = "ScreenTransitionManager";

    /**
     * Starts the transition tracing.It the callers responsibility to call the method with a
     * non null {@link Activity} argument. It is recommended to use {@link #beginTransition(Activity, String)} in
     * case multiple activity of same type exist in Activity Stack to prevent naming conflict and incorrect response
     *
     * @param activity A Non Null {@link Activity}
     */
    public void beginTransition(@NonNull Activity activity) {
        String screenName = getScreenNameFromActivity(activity);
        beginTransition(activity, screenName);
    }

    /**
     * Starts the transition tracing.It the callers responsibility to call the method with a unique screen name
     * to prevent naming collision.If the passed screen name already exist,then tracing will not be started for the
     * subsequent screen of the same name
     *
     * @param screenName Name of the screen for which transition tracing is to started
     */
    public void beginTransition(@NonNull Activity activity, @NonNull String screenName) {
        boolean doesScreenNameAlreadyExist = screenTransitionStats.containsKey(screenName);
        if (!doesScreenNameAlreadyExist) {
            TransitionStat transitionStat = TransitionStat.beginTransitionStat(activity.hashCode());
            screenTransitionStats.put(screenName, transitionStat);
        }
    }

    /**
     * Stop tracing the screen transition and flushes the information onto the console.Only call this version of
     * endTransition if and only you called {@link #beginTransition(Activity)}  else call
     * {@link #endTransition(Activity, String)}. This is done to prevent incorrect information be flushed out in case of
     * using difference instance of same activity
     *
     * @param activity {@link Activity} for which transition need to be stopped
     */
    public void endTransition(@NonNull Activity activity) {
        String screenName = getScreenNameFromActivity(activity);
        endTransition(activity, screenName);
    }

    /**
     * Stop tracing the screen transition and flushes the information onto the console.Only call this version of
     * endTransition if and only you called {@link #beginTransition(Activity, String)}  else call
     * {@link #endTransition(Activity)}. This is done to prevent incorrect information be flushed out in case of
     * using difference instance of same activity
     *
     * @param activity   {@link Activity} for which transition need to be stopped
     * @param screenName Name of the screen which was used during beginTransition
     */

    public void endTransition(@NonNull Activity activity, @NonNull String screenName) {
        TransitionStat transitionStat = screenTransitionStats.get(screenName);
        if (transitionStat != null && !transitionStat.isFlushed() && activity.hashCode() == transitionStat.getId()) {
            endTransitionWhenStatsExist(activity, screenName, transitionStat);
        }
    }

    /**
     * Handle stopping transition if the {@link TransitionStat} exist for the given Screen Name or {@link Activity}.
     * Logs information onto Logcat if the stats has not been flushed previously and takes the screenshot
     *
     * @param activity       Activity for which transition is to be stopped
     * @param screenName     Screen Name for which transition is to stopped
     * @param transitionStat Transition information
     */
    private void endTransitionWhenStatsExist(@NonNull Activity activity, @NonNull String screenName,
                                             @NonNull TransitionStat transitionStat) {
        TransitionStat updatedStat = transitionStat.copy(SystemClock.elapsedRealtime());
        screenTransitionStats.put(screenName, updatedStat);
        flushTransitionDetails(screenName, updatedStat);
        ScreenshotManager.getInstance().takeAndSave(activity, screenName);
    }

    private void flushTransitionDetails(@NonNull String screenName, @NonNull TransitionStat transitionStat) {
        try {
            long duration = transitionStat.transitionDuration();
            Log.d(tag, String.format(Locale.ENGLISH, "%s took %d milliseconds to complete transition", screenName, duration));
            // Remove Transition Stats information after is logged out
            screenTransitionStats.remove(screenName);
        } catch (IllegalStateException e) {
            Log.e(tag, "Failed to log transition details", e);
        }
    }

    /**
     * Fetches the screen name for a given activity
     *
     * @param activity {@link Activity}
     * @return Name of the screen
     */
    @NonNull
    private String getScreenNameFromActivity(@NonNull Activity activity) {
        return activity.getComponentName().getClassName();
    }
}


