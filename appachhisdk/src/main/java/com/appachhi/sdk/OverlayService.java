package com.appachhi.sdk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.appachhi.sdk.sync.SyncManager;
import com.appachhi.sdk.ui.ConfigurationActivity;

import java.util.Collections;
import java.util.List;

public class OverlayService extends Service {
    private static final String TAG = "OverlayService";
    private static final String NOTIFICATION_CHANNEL_ID = "com.appachhi.sdk.monitor";
    private static final int NOTIFICATION_ID = 14;
    private static final String CONFIG_KEY = "OverlayServiceConfig";
    private static final String ACTION_SHOW_SUFFIX = ".overlay.ACTION_SHOW";
    private static final String ACTION_HIDE_SUFFIX = ".overlay.ACTION_HIDE";
    private static String actionShow;
    private static String actionHide;

    private OverlayServiceBinder binder = new OverlayServiceBinder();
    private OverlayViewManager overlayViewManager;
    private List<FeatureModule> featureModules = Collections.emptyList();
    private NotificationManager notificationManager;
    private Appachhi.Config config;
    private boolean modulesStarted;

    // Temporart Work Around for Sync
    SyncManager syncManager;

    public SharedPreferences appachhiPerf;


    public static void startAndBind(Context context, Appachhi.Config config, SharedPreferences appachhiPref) {
        Intent intent = createIntent(context);
        intent.putExtra(CONFIG_KEY, config);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }

    public static void showOverlay(Context context) {
        if (actionShow != null) {
            Intent intent = new Intent(actionShow);
            context.sendBroadcast(intent);
        }

    }

    public static void hideOverlay(Context context) {
        if (actionHide != null) {
            Intent intent = new Intent(actionHide);
            context.sendBroadcast(intent);
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, OverlayService.class);
    }

    public static void stop(Context context) {
        context.stopService(createIntent(context));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Appachhi.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        appachhiPerf = getApplicationContext().getSharedPreferences("appachhi_pref", Context.MODE_PRIVATE);


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();

        // Intent Filter
        String packageName = getPackageName();
        actionShow = packageName + ACTION_SHOW_SUFFIX;
        actionHide = packageName + ACTION_HIDE_SUFFIX;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(actionShow);
        intentFilter.addAction(actionHide);
        registerReceiver(receiver, intentFilter);

        // Create sync manager and start sync

        syncManager = SyncManager.create(this.getApplication());
        // call trigger metrics here - Should pull values from the shared pref and start/stop each metric based on values.

        //  getMetricDetails();
        syncManager.startSync();
    }

    private void getMetricDetails() {
        Log.d(TAG, "getMetricDetails: Get Metric details called. Initial run");
        String fps = appachhiPerf.getString("fps_status", "false");
        String gcs = appachhiPerf.getString("gcs_status", "false");
        String memory_leak = appachhiPerf.getString("memory_leak_status", "false");
        String network_usage = appachhiPerf.getString("network  _usage_status", "false");
        String memory_usage = appachhiPerf.getString("memory_usage_status", "false");

        String battery_stats = appachhiPerf.getString("battery_stats_status", "false");

        triggerMetrics(fps, gcs, memory_leak, network_usage, memory_usage, battery_stats);
    }

    private void triggerMetrics(String fps, String gcs, String memory_leak, String network_usage,
                                String memory_usage, String battery_stats) {

        //Update the values to the shared pref


        Log.d(TAG, "Overlay Service triggerMetrics: Called : FPS = " + fps + " + GC : " + gcs + " : Memory Leak :  "
                + memory_leak + " : Network Usage : " + network_usage + " : Memory Usage : " + memory_usage +
                " : Battery Stats : " + battery_stats);
        //Appachhi.getInstance().checkConfigStats();
        //    Appachhi appachhi = new Appachhi();


        if (fps.equals("true")) {
            Appachhi.getInstance().addFpsModule(true);
            Appachhi.getInstance().addFrameDropModule(true);
        } else {
            Appachhi.getInstance().addFpsModule(false);
            Appachhi.getInstance().addFrameDropModule(false);

        }

        if (gcs.equals("true")) {
            Appachhi.getInstance().addGCModule(true);
        } else {
            Appachhi.getInstance().addGCModule(false);
        }

        if (memory_leak.equals("true")) {
            Appachhi.getInstance().addMemoryLeakModule(true);
        } else {
            Appachhi.getInstance().addMemoryLeakModule(false);
        }

        if (network_usage.equals("true")) {
            Appachhi.getInstance().addNetworkUsageModule(true);
        } else {
            Appachhi.getInstance().addNetworkUsageModule(false);
        }

        if (memory_usage.equals("true")) {
            Appachhi.getInstance().addMemoryInfoModule(true);
        } else {
            Appachhi.getInstance().addMemoryInfoModule(false);
        }

        if (battery_stats.equals("true")) {
            Appachhi.getInstance().addBatteryStatsModule(true);

        } else {
            Appachhi.getInstance().addBatteryStatsModule(false);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        config = intent.getParcelableExtra(CONFIG_KEY);
        // no need to restart this service
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        cancelNotification();
        stopModules();
        overlayViewManager.hideDebugSystemOverlay();

        // Stop Sync
        syncManager.stopSync();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "onTaskRemoved");
        }
        stopSelf();
        EventBus.getInstance().post(Appachhi.ACTION_UNBIND);
    }


    public void setOverlayViewManager(OverlayViewManager overlayViewManager) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "setOverlayViewManager");
        }
        this.overlayViewManager = overlayViewManager;
        if (config.isOverlayAllowed()) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "showDebugSystemOverlay");
            }
            //  overlayViewManager.showDebugSystemOverlay();
            if (config.showNotification()) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "showNotification");
                }
                showNotification();
            }
        }
    }

    public void setOverlayModules(List<FeatureModule> overlayModules) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "setOverlayModules");
        }
        this.featureModules = overlayModules;
    }

    public void startModules() {
        //Trigger Startup Time and Logs by default
        if (Appachhi.DEBUG) {
            Log.d(TAG, "startModules");
        }
        if (!modulesStarted) {
            for (FeatureModule overlayModule : featureModules) {
                Log.d(TAG, "startModules: Overlay Service : " + overlayModule.toString());
                overlayModule.start();
            }
            modulesStarted = true;
        }
    }

    public void stopModules() {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "stopModules");
        }
        if (modulesStarted) {
            for (FeatureModule featureModule : featureModules) {
                featureModule.stop();
            }
            modulesStarted = false;
        }
    }

    public void updateNotification() {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "updateNotification");
        }
        showNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                if (Appachhi.DEBUG) {
                    Log.d(TAG, "createNotificationChannel");
                }
                String channelName = getString(R.string.appachhi_monitor_notification_channel);
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        channelName, NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Appachhi.DEBUG) {
                Log.d(TAG, String.format("onReceive: %s", action));
            }
            if (actionShow.equals(action)) {
                overlayViewManager.showDebugSystemOverlay();
                startModules();
                // update notification
                showNotification();
            } else if (actionHide.equals(action)) {
                stopModules();
                overlayViewManager.hideDebugSystemOverlay();
                // update notification
                showNotification();
            }
        }
    };


    private void showNotification() {
        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                new Notification.Builder(this, NOTIFICATION_CHANNEL_ID) :
                new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_monitor)
                .setOngoing(true)
                .setContentTitle(getString(R.string.appachhi_monitor_notification_title))
                .setContentText(getString(R.string.appachhi_monitor_notification_body))
                .setContentIntent(getNotificationIntent(null));
        // show the notification
        startForeground(NOTIFICATION_ID, builder.build());
    }

    private PendingIntent getNotificationIntent(String action) {
        if (Appachhi.DEBUG) {
            Log.d(TAG, String.format("getNotificationIntent %s", action));
        }
        if (action == null) {
            return ConfigurationActivity.getPendingIntent(this);
        } else {
            Intent intent = new Intent(action);
            return PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private void cancelNotification() {
        if (Appachhi.DEBUG) {
            Log.d(TAG, "cancelNotification");
        }
        stopForeground(true);
    }

    class OverlayServiceBinder extends Binder {
        OverlayService getService() {
            return OverlayService.this;
        }
    }
}

