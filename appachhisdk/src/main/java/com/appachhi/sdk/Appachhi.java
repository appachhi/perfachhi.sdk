package com.appachhi.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
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
import com.appachhi.sdk.monitor.framedrop.FrameDropFeatureModule;
import com.appachhi.sdk.monitor.logs.LogsFeatureModule;
import com.appachhi.sdk.monitor.memory.GCInfoFeatureModule;
import com.appachhi.sdk.monitor.memory.MemoryInfoFeatureModule;
import com.appachhi.sdk.monitor.memoryleak.MemoryLeakFeatureModule;
import com.appachhi.sdk.monitor.network.NetworkFeatureModule;
import com.appachhi.sdk.monitor.screen.ScreenCaptureFeatureModule;
import com.appachhi.sdk.monitor.startup.StartupDataModule;
import com.appachhi.sdk.monitor.startup.StartupFeatureModule;
import com.appachhi.sdk.monitor.startup.StartupTimeManager;
import com.appachhi.sdk.sync.SessionManager;

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
    private static final String TAG = "Perfachhi-DEBUG";

    private static Appachhi instance;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "onServiceConnected");
            }
            // We've bound to OverlayService, cast the IBinder and get OverlayService getInstance
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
    private final EventBus.Listener receiver = new EventBus.Listener() {
        @Override
        public void onChange(String action) {
            if (ACTION_UNBIND.equals(action)) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "Serivce Unbind Broadcast");
                }
                unBindRequestReceived = true;
                unbindFromDebugOverlayService();
            }
        }
    };

    private List<FeatureModule> featureModules;

    private OverlayViewManager overlayViewManager;

    private OverlayService overlayService;

    private Config config;

    private FeatureConfigManager featureConfigManager;

    private Application application;
    private boolean unBindRequestReceived;

    private AppachhiDB db;

    private ExecutorService dbExecutor;

    private SessionManager sessionManager;

    private MethodTraceSavingManager methodTraceSavingManager;

    private HttpMetricSavingManager httpMetricSavingManager;
    @SuppressWarnings("FieldCanBeLocal")
    private ActivityCallbacks activityCallbacks;
    public StartupTimeManager startupTimeManager;
    private boolean warmStartStatus = true;
    private boolean passedOnCreate = true;
    private StartupDataModule startupDataModule;


    private Appachhi(Application application, Config config) {
        this.application = application;
        this.config = config;

        // Creates DB
        db = AppachhiDB.getInstance(application);
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


        startupTimeManager = new StartupTimeManager(application.getApplicationContext());

    }

    @SuppressWarnings("UnusedReturnValue")
    public static Appachhi init(Application application) {
        DEBUG = true;
        instance = new Appachhi(application, new Config(true, true));
        return instance;
    }

    public static Appachhi getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot request Appachhi getInstance before calling init()");
        }

        return instance;
    }


    public SessionManager getSessionManager() {
        return sessionManager;
    }


    private MethodTraceSavingManager getMethodTraceSavingManager() {
        return methodTraceSavingManager;
    }


    private HttpMetricSavingManager getHttpMetricSavingManager() {
        return httpMetricSavingManager;
    }


    public FeatureConfigManager getFeatureConfigManager() {
        return featureConfigManager;
    }


    public AppachhiDB getDb() {
        return db;
    }

    public static MethodTrace newTrace(String traceName) {
        return new MethodTrace(traceName, Appachhi.getInstance().getMethodTraceSavingManager());
    }

    public static HttpMetric newHttpTrace() {
        return new HttpMetric(Appachhi.getInstance().getHttpMetricSavingManager());
    }

    private List<FeatureModule> addModules(Application application) {

        Log.d(TAG, "addModules: Called");

        List<FeatureModule> featureModules = new LinkedList<>();
        featureModules.add(new MemoryInfoFeatureModule(application, db.memoryDao(), dbExecutor, sessionManager));
        featureModules.add(new GCInfoFeatureModule(db.gcDao(), dbExecutor, sessionManager));
        featureModules.add(new NetworkFeatureModule(db.networkDao(), dbExecutor, sessionManager));
        featureModules.add(new CpuUsageInfoFeatureModule(db.cpuUsageDao(), dbExecutor, sessionManager));
        featureModules.add(new FpsFeatureModule(db.fpsDao(), dbExecutor, sessionManager));
        featureModules.add(new MemoryLeakFeatureModule(application, db.memoryLeakDao(), dbExecutor, sessionManager));
        featureModules.add(new FrameDropFeatureModule(application, dbExecutor, sessionManager, db.frameDropDao()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            featureModules.add(new ScreenCaptureFeatureModule(application.getApplicationContext(), sessionManager, db.screenshotDao(), dbExecutor));
        }
        featureModules.add(new ScreenTransitionFeatureModule(ScreenTransitionManager.getInstance(), db.screenTransitionDao(), dbExecutor, sessionManager));
        featureModules.add(new LogsFeatureModule(application.getApplicationContext(), sessionManager, db.logsDao(), dbExecutor));
        featureModules.add(new StartupFeatureModule(application.getApplicationContext(), db.startupDao(), dbExecutor, sessionManager));
        return featureModules;
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
        EventBus.getInstance().register(receiver);
    }

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
        EventBus.getInstance().unRegister(receiver);
    }

    public static class Config implements Parcelable {
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
        private final boolean overlayAllowed;
        private final boolean showNotification;

        Config(boolean overlayAllowed, boolean showNotification) {
            this.overlayAllowed = overlayAllowed;
            this.showNotification = showNotification;
        }

        Config(Parcel in) {
            this.overlayAllowed = in.readByte() != 0;
            this.showNotification = in.readByte() != 0;
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

            passedOnCreate = true;

            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("onActivityCreated: %s", activity.getComponentName()));
            }
            if (!config.isOverlayAllowed()) {
                OverlayViewManager.OverlayViewAttachStateChangeListener listener =
                        overlayViewManager.createAttachStateChangeListener();
                activity.getWindow().getDecorView().addOnAttachStateChangeListener(listener);
                attachStateChangeListeners.put(activity, listener);
            }

            if (savedInstanceState != null) {

                warmStartStatus = savedInstanceState.getBoolean("warmStartStatus");
            }

            if (passedOnCreate && warmStartStatus) {
                startupTimeManager.setWarm_starttime(SystemClock.elapsedRealtime());
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

            if (numRunningActivities == 1) {
                if (startupTimeManager != null) {

                    if (startupTimeManager.isFirstLaunch()) {

                        long coldStarttime = startupTimeManager.getCold_starttime();
                        long coldstartValue = SystemClock.elapsedRealtime() - coldStarttime;

                        Log.d(TAG, "onActivityResumed: Cold start time : " + coldStarttime + ": Cold Start End time = " + SystemClock.elapsedRealtime());

                        startupTimeManager.setCold_startResult(coldstartValue);
                        Log.d(TAG, "onActivityResumed: COLD : " + startupTimeManager.getCold_starttime() + " : ColdStart Value : " + startupTimeManager.getCold_startResult());

                        startupTimeManager.setFirstLaunch(false);
                    }


                } else {
                    Log.d(TAG, "onActivityResumed: StartupTimeManager is null");
                }

                if (passedOnCreate && warmStartStatus) {
                    Log.d(TAG, "onActivityResumed: Warm Start time : " + startupTimeManager.getWarm_starttime() + " End time : " + SystemClock.elapsedRealtime());
                    long warmStartResult = SystemClock.elapsedRealtime() - startupTimeManager.getWarm_starttime();
                    startupTimeManager.setWarmStart_result(warmStartResult);
                    Log.d(TAG, "onActivityResumed: WarmStartResult = " + warmStartResult);
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

            passedOnCreate = false;

            decrementNumRunningActivities();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            outState.putBoolean("warmStartStatus", false);
            Log.d(TAG, "onActivitySaveInstanceState: called : Warm start is " + outState.getBoolean("warmStartStatus"));

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
}