package com.appachhi.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.SaveRequest;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appachhi.sdk.instrument.trace.MethodTrace;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfoFeatureModule;
import com.appachhi.sdk.monitor.memory.GCInfoFeatureModule;
import com.appachhi.sdk.monitor.memory.MemoryInfoFeatureModule;
import com.appachhi.sdk.monitor.network.NetworkFeatureModule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Appachhi {
    public static final String ACTION_UNBIND = "com.appachhi.sdk.overlay.ACTION_UNBIND";
    public static boolean DEBUG = false;
    private static final String TAG = "Appachhi-DEBUG";
    private List<FeatureModule> featureModules;
    private OverlayViewManager overlayViewManager;
    private OverlayService overlayService;
    private Config config;
    private Application application;
    private boolean unBindRequestReceived;
    private ActivityCallbacks activityCallbacks;

    private Appachhi(Application application, List<FeatureModule> featureModules, Config config) {
        this.application = application;
        this.featureModules = featureModules;
        this.config = config;

        overlayViewManager = new OverlayViewManager(application, config);
        overlayViewManager.setFeatureModules(featureModules);
        startAndBindDebugOverlayService();
        activityCallbacks = new ActivityCallbacks();
        application.registerActivityLifecycleCallbacks(activityCallbacks);
    }

    public static Appachhi init(@NonNull Application application) {
        DEBUG = true;
        List<FeatureModule> modules = Arrays.<FeatureModule>asList(
                new MemoryInfoFeatureModule(application),
                new GCInfoFeatureModule(),
                new NetworkFeatureModule(),
                new CpuUsageInfoFeatureModule());
        return new Appachhi(application, modules, new Config(true, true));
    }

    public static MethodTrace startMethodTrace(String className, String methodName) {
        return new MethodTrace(className, methodName);
    }

    public static MethodTrace startMethodTrace(String traceName) {
        return new MethodTrace(traceName);
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
            // We've bound to DebugOverlayService, cast the IBinder and get DebugOverlayService instance
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
    final BroadcastReceiver receiver = new BroadcastReceiver() {
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

}
