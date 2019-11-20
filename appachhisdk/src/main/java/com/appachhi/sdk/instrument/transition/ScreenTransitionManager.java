package com.appachhi.sdk.instrument.transition;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.appachhi.sdk.Appachhi;

import java.util.WeakHashMap;

/**
 * Class responsible for handling the screen transition tracing.This class can trace the screen transition for a given
 * activity as well as generic screen name. Precession of the time duration is up to millis i.e all time information
 * provided by this API will be in {@link java.util.concurrent.TimeUnit#MILLISECONDS}
 */
@SuppressWarnings("JavadocReference")
final public class ScreenTransitionManager {
    private static final String TAG = "ScrTransitionMgr";
    private static ScreenTransitionManager instance;
    private ScreenshotManager screenshotManager;
    private OnTransitionListener onTransitionListener;

    private ScreenTransitionManager(ScreenshotManager screenshotManager) {
        this.screenshotManager = screenshotManager;
    }

    @Keep
    public static ScreenTransitionManager getInstance() {
        synchronized (ScreenTransitionManager.class) {
            if (instance == null) {
                instance = new ScreenTransitionManager(new ScreenshotManager());
            }
            return instance;
        }
    }

    private WeakHashMap<String, TransitionStat> screenTransitionStats = new WeakHashMap<>();

    /**
     * Starts the transition tracing.It the callers responsibility to call the method with a
     * non null {@link Activity} argument. It is recommended to use {@link #beginTransition(Activity, String)} in
     * case multiple activity of same type exist in Activity Stack to prevent naming conflict and incorrect response
     *
     * @param activity A Non Null {@link Activity}
     */
    @Keep
    public void beginTransition(@NonNull Activity activity) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "beginTransition");
        }
        String screenName = getScreenNameFromActivity(activity);
        beginTransition(activity, screenName);
    }

    /**
     * Starts the transition tracing.It the callers responsibility to call the method with a unique screen name
     * to prevent naming collision.If the passed screen name already exist,then tracing will not be started for the
     * subsequent screen of the same name.
     * <p>
     * Also  notifies the listener about if it is attached
     *
     * @param screenName Name of the screen for which transition tracing is to started
     */
    @Keep
    public void beginTransition(@NonNull Activity activity, @NonNull String screenName) {
        Log.d(TAG, "beginTransition");
        boolean doesScreenNameAlreadyExist = screenTransitionStats.containsKey(screenName);
        if (!doesScreenNameAlreadyExist) {
            TransitionStat transitionStat = TransitionStat.beginTransitionStat(screenName, activity.hashCode());
            screenTransitionStats.put(screenName, transitionStat);
            if (this.onTransitionListener != null) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "Notifying listener");
                }
                onTransitionListener.onTransitionBegin(screenName);
            } else {
                Log.d(TAG, "No Listener attached");
            }
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
    @Keep
    public void endTransition(@NonNull Activity activity) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "endTransition");
        }
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

    @Keep
    public void endTransition(@NonNull Activity activity, @NonNull String screenName) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "endTransition");
        }
        TransitionStat transitionStat = screenTransitionStats.get(screenName);
        if (transitionStat != null && !transitionStat.isFlushed() && activity.hashCode() == transitionStat.getId()) {
            endTransitionWhenStatsExist(activity, screenName, transitionStat);
        }
    }

    /**
     * Return the attached onTransitionListener
     *
     * @return null in case the onTransitionListener has not been attached
     */
    OnTransitionListener getOnTransitionListener() {
        return onTransitionListener;
    }

    /**
     * Register a listener to get notified whenever  their is a screen transition
     *
     * @param onTransitionListener {@link OnTransitionListener}
     */
    @Keep
    synchronized void registerListener(@NonNull OnTransitionListener onTransitionListener) {
        if (Appachhi.DEBUG) {
            //noinspection ConstantConditions
            Log.d(TAG, String.format("registerListener %s", onTransitionListener!=null));
        }
        this.onTransitionListener = onTransitionListener;
    }

    /**
     * Removes any screen screen transition attached
     *
     * @param onTransitionListener {@link OnTransitionListener}
     */
    @Keep
    synchronized void unRegister(@NonNull OnTransitionListener onTransitionListener) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "unRegister");
        }
        this.onTransitionListener = null;
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
        Log.d(TAG, "endTransitionWhenStatsExist");
        TransitionStat updatedStat = transitionStat.copy(SystemClock.elapsedRealtime());

        // Notify the screen transition has been completed
        if (this.onTransitionListener != null) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "endTransitionWhenStatsExist");
            }
            onTransitionListener.onTransitionEnd(updatedStat);
        } else {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "Not Notifying as listener is not registered: ");
            }
        }
        screenTransitionStats.remove(screenName);
        // Take a screenshot
        if (Appachhi.DEBUG) {
            Log.d(TAG, "Take ScreenshotEntity");
        }
        screenshotManager.takeAndSave(activity, screenName);

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

    static abstract class OnTransitionListener {

        abstract void onTransitionBegin(String name);

        abstract void onTransitionEnd(TransitionStat transitionStat);
    }
}


