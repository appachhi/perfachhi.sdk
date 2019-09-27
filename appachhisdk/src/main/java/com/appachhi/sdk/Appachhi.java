package com.appachhi.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.instrument.network.internal.HttpMetric;
import com.appachhi.sdk.instrument.network.internal.HttpMetricSavingManager;
import com.appachhi.sdk.instrument.trace.MethodTrace;
import com.appachhi.sdk.instrument.trace.MethodTraceSavingManager;
import com.appachhi.sdk.instrument.transition.ScreenTransitionFeatureModule;
import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfoFeatureModule;
import com.appachhi.sdk.monitor.fps.FpsFeatureModule;
import com.appachhi.sdk.monitor.memory.GCInfoFeatureModule;
import com.appachhi.sdk.monitor.memory.MemoryInfoFeatureModule;
import com.appachhi.sdk.monitor.memoryleak.MemoryLeakFeatureModule;
import com.appachhi.sdk.monitor.network.NetworkFeatureModule;
import com.appachhi.sdk.monitor.screen.ScreenCaptureFeatureModule;
import com.appachhi.sdk.sync.SessionManager;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Appachhi {
    // Constants
    static final String ACTION_UNBIND = "com.appachhi.sdk.overlay.ACTION_UNBIND";
    public static boolean DEBUG = false;
    private static final String TAG = "Appachhi-DEBUG";

    private static Appachhi instance;

    @NonNull
    private List<FeatureModule> featureModules;
    @NonNull
    private OverlayViewManager overlayViewManager;
    @Nullable
    private OverlayService overlayService;
    @NonNull
    private Config config;
    @NonNull
    private FeatureConfigManager featureConfigManager;
    @NonNull
    private Application application;
    private boolean unBindRequestReceived;
    @NonNull
    private AppachhiDB db;
    @NonNull
    private ExecutorService dbExecutor;
    @NonNull
    private SessionManager sessionManager;
    @NonNull
    private MethodTraceSavingManager methodTraceSavingManager;
    @NonNull
    private HttpMetricSavingManager httpMetricSavingManager;
    @SuppressWarnings("FieldCanBeLocal")
    private ActivityCallbacks activityCallbacks;

    @SuppressWarnings("UnusedReturnValue")
    public static Appachhi init(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return null;
        }
        DEBUG = true;
        instance = new Appachhi(application, new Config(true, true));
        return instance;
    }

    @Keep
    public static Appachhi getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot request Appachhi instance before calling init()");
        }
        return instance;
    }

    @Keep
    public static MethodTrace newTrace(String traceName) {
        return new MethodTrace(traceName, Appachhi.getInstance().getMethodTraceSavingManager());
    }

    @Keep
    public static HttpMetric newHttpTrace() {
        return new HttpMetric(Appachhi.getInstance().getHttpMetricSavingManager());
    }

    private Appachhi(@NonNull Application application, @NonNull Config config) {
        this.application = application;
        this.config = config;

        // Creates DB
        db = AppachhiDB.create(application);
        // Creates DatabaseExecutor
        dbExecutor = Executors.newSingleThreadExecutor();

        // Update the current session
        sessionManager = new SessionManager(this.application, db.sessionDao(), dbExecutor);
        sessionManager.newSession();

        methodTraceSavingManager = new MethodTraceSavingManager(db.methodTraceDao(), dbExecutor, sessionManager);
        httpMetricSavingManager = new HttpMetricSavingManager(db.apiCallDao(), dbExecutor, sessionManager);

        // Build the feature modules
        this.featureModules = addModules(application);
        // Build Feature Configuration Manager
        featureConfigManager = new FeatureConfigManager(this.featureModules);

        // Build overlay Manager
        overlayViewManager = new OverlayViewManager(application, config);
        overlayViewManager.setFeatureModules(featureModules);

        // Start overlay
        startAndBindDebugOverlayService();
        activityCallbacks = new ActivityCallbacks();
        application.registerActivityLifecycleCallbacks(activityCallbacks);
    }

    @NonNull
    @Keep
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @NonNull
    private MethodTraceSavingManager getMethodTraceSavingManager() {
        return methodTraceSavingManager;
    }

    @NonNull
    private HttpMetricSavingManager getHttpMetricSavingManager() {
        return httpMetricSavingManager;
    }

    @NonNull
    @Keep
    public FeatureConfigManager getFeatureConfigManager() {
        return featureConfigManager;
    }

    @NonNull
    @Keep
    public AppachhiDB getDb() {
        return db;
    }

    private List<FeatureModule> addModules(Application application) {
        List<FeatureModule> featureModules = new LinkedList<>();
        featureModules.add(new MemoryInfoFeatureModule(application, db.memoryDao(), dbExecutor, sessionManager));
        featureModules.add(new GCInfoFeatureModule(db.gcDao(), dbExecutor, sessionManager));
        featureModules.add(new NetworkFeatureModule(db.networkDao(), dbExecutor, sessionManager));
        featureModules.add(new CpuUsageInfoFeatureModule(db.cpuUsageDao(), dbExecutor, sessionManager));
        featureModules.add(new FpsFeatureModule(db.fpsDao(), dbExecutor, sessionManager));
        featureModules.add(new MemoryLeakFeatureModule(application, db.memoryLeakDao(), dbExecutor, sessionManager));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            featureModules.add(new ScreenCaptureFeatureModule(application.getApplicationContext(), sessionManager, db.screenshotDao(), dbExecutor));
        }
        featureModules.add(new ScreenTransitionFeatureModule(ScreenTransitionManager.getInstance(), db.screenTransitionDao(), dbExecutor, sessionManager));
        return featureModules;
    }

    /**
     * This class decides when the modules should startAndBind observing for the data changes and when to stop
     * it
     * <p>
     * As of now,The Module will be publishes data once there is an active activity
     * i.e Activvity onResume() has been called and will continue to do so until all the
     * activity has been stopped i.e their Activity onPause() is called for all the activity
     */
    private class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {
        private Map<Activity, OverlayViewManager.OverlayViewAttachStateChangeListener> attachStateChangeListeners;

        private int numRunningActivities;

        ActivityCallbacks() {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "ActivityCallbacks");
            }
            if (!config.isOverlayAllowed()) {
                attachStateChangeListeners = new WeakHashMap<>();
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("onActivityCreated: %s", activity.getComponentName()));
            }
            if (!config.isOverlayAllowed()) {
                OverlayViewManager.OverlayViewAttachStateChangeListener listener =
                        overlayViewManager.createAttachStateChangeListener();
                activity.getWindow().getDecorView().addOnAttachStateChangeListener(listener);
                attachStateChangeListeners.put(activity, listener);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("onActivityStarted: %s", activity.getComponentName()));
            }
            incrementNumRunningActivities();
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (config.isOverlayAllowed()) {
                if (overlayViewManager.isOverlayPermissionRequested() &&
                        OverlayViewManager.canDrawOnSystemLayer(activity, OverlayViewManager.getWindowTypeForOverlay(true))) {
                    overlayViewManager.showDebugSystemOverlay();
                    if (overlayService != null) {
                        overlayService.updateNotification();
                    }
                }
            } else {
                OverlayViewManager.OverlayViewAttachStateChangeListener listener = attachStateChangeListeners.get(activity);
                if (listener != null) {
                    listener.onActivityResumed();
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "onActivityPaused: ");
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "onActivityStopped");
            }
            decrementNumRunningActivities();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (attachStateChangeListeners != null) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "onActivityDestroyed");
                }
                attachStateChangeListeners.remove(activity);
            }
        }

        private void incrementNumRunningActivities() {
            if (numRunningActivities == 0) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "incrementNumRunningActivities: 0 Running Activity");
                }
                // app is in foreground
                if (config.isOverlayAllowed()) {
                    if (overlayService == null && unBindRequestReceived) {
                        if (Appachhi.DEBUG) {
                            Log.d(TAG, "incrementNumRunningActivities: Binding Request");
                        }
                        // service already un-bound by a explicit request, but restart here since it is now in foreground
                        startAndBindDebugOverlayService();
                        unBindRequestReceived = false;
                    }
                } else {
                    // restart modules since it may have been stopped
                    if (overlayService != null) {
                        if (Appachhi.DEBUG) {
                            Log.d(TAG, "incrementNumRunningActivities: Starting Module");
                        }
                        overlayService.startModules();
                    }
                }
            }
            numRunningActivities++;
            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("incrementNumRunningActivities: Total Running Activity %d", numRunningActivities));
            }
        }

        private void decrementNumRunningActivities() {
            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("decrementNumRunningActivities: Total Running Activity %d", numRunningActivities));
            }
            numRunningActivities--;
            if (numRunningActivities <= 0) {
                numRunningActivities = 0;
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "decrementNumRunningActivities: 0 Running Activity");
                }
                // apps is in background
                if (!config.isOverlayAllowed() && overlayService != null) {
                    if (Appachhi.DEBUG) {
                        Log.d(TAG, "App in Backgroound");
                    }
                    overlayService.stopModules();
                }
            }
        }
    }

    private void startAndBindDebugOverlayService() {
        OverlayService.startAndBind(application, config);
        bindToDebugOverlayService();
    }

    private void bindToDebugOverlayService() {
        boolean bound = application.bindService(OverlayService.createIntent(application),
                serviceConnection, Context.BIND_AUTO_CREATE);
        if (!bound) {
            throw new RuntimeException("Could not bind the OverlayService");
        }
        LocalBroadcastManager.getInstance(application).registerReceiver(receiver, new IntentFilter(ACTION_UNBIND));
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "onServiceConnected");
            }
            // We've bound to OverlayService, cast the IBinder and get OverlayService instance
            OverlayService.OverlayServiceBinder binder = (OverlayService.OverlayServiceBinder) serviceBinder;
            overlayService = binder.getService();
            overlayService.setOverlayModules(featureModules);
            overlayService.setOverlayViewManager(overlayViewManager);
            overlayService.startModules();
        }

        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        // So, this is not called when the client unbinds.
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_UNBIND.equals(intent.getAction())) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "Serivce Unbind Broadcast");
                }
                unBindRequestReceived = true;
                unbindFromDebugOverlayService();
            }
        }
    };

    private void unbindFromDebugOverlayService() {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "unbindFromDebugOverlayService ");
        }
        if (overlayService != null) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "Unbing when service is not null ");
            }
            application.unbindService(serviceConnection);
            overlayService = null;
        }
        LocalBroadcastManager.getInstance(application).unregisterReceiver(receiver);
    }

    public static class Config implements Parcelable {
        private final boolean overlayAllowed;
        private final boolean showNotification;

        Config(boolean overlayAllowed, boolean showNotification) {
            this.overlayAllowed = overlayAllowed;
            this.showNotification = showNotification;
        }

        boolean isOverlayAllowed() {
            return overlayAllowed;
        }

        boolean showNotification() {
            return showNotification;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.overlayAllowed ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showNotification ? (byte) 1 : (byte) 0);
        }

        Config(Parcel in) {
            this.overlayAllowed = in.readByte() != 0;
            this.showNotification = in.readByte() != 0;
        }

        public static final Creator<Config> CREATOR = new Creator<Config>() {
            @Override
            public Config createFromParcel(Parcel source) {
                return new Config(source);
            }

            @Override
            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
    }
}
