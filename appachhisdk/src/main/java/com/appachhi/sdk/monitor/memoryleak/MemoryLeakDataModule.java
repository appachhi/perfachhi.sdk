package com.appachhi.sdk.monitor.memoryleak;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.R;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.internal.DisplayLeakActivity;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static android.text.format.Formatter.formatShortFileSize;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.classSimpleName;

@SuppressLint("PrivateResource")
public class MemoryLeakDataModule extends BaseDataModule<List<MemoryLeakInfo>> {

    static final String LEAK_BROADCAST_ACTION = "com.appachhi.sdk.memoryleak.ACTION_LEAK_FOUND";
    static final String LEAK_EXTRA_HEAP_DUMP = "HEAP_DUMP";
    static final String LEAK_EXTRA_LEAK_INFO = "LEAK_INFO";
    static final String LEAK_EXTRA_ANALYSIS_RESULT = "ANALYSIS_RESULT";
    private static final String NOTIFICATION_CHANNEL_ID = "com.appachhi.sdk.memoryleak";
    public static final String TAG = "MemoryLeakDataModule";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Appachhi.DEBUG) {
                Log.d(TAG, "onReceive: Leak Info Received");
            }
            HeapDump heapDump = (HeapDump) intent.getSerializableExtra(LEAK_EXTRA_HEAP_DUMP);
            String leakInfo = intent.getStringExtra(LEAK_EXTRA_LEAK_INFO);
            AnalysisResult analysisResult = (AnalysisResult) intent.getSerializableExtra(LEAK_EXTRA_ANALYSIS_RESULT);
            onLeakInformationReceived(heapDump, analysisResult, leakInfo);
        }
    };

    private List<MemoryLeakInfo> allMemoryLeakInfo = new ArrayList<>();
    private Application application;
    private IntentFilter intentFilter = new IntentFilter(LEAK_BROADCAST_ACTION);

    MemoryLeakDataModule(Application application) {
        this.application = application;
        if (!LeakCanary.isInAnalyzerProcess(application)) {
            LeakCanary.refWatcher(application)
                    .listenerServiceClass(AppachhiMemoryLeakService.class)
                    .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                    .buildAndInstall();
        }
    }

    @Nullable
    @Override
    protected List<MemoryLeakInfo> getData() {
        return allMemoryLeakInfo;
    }

    @Override
    public void start() {
        LocalBroadcastManager.getInstance(application).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void stop() {
        LocalBroadcastManager.getInstance(application).unregisterReceiver(receiver);
    }

    private void onLeakInformationReceived(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        String contentTitle = getContentTitle(result);
        String contentText = application.getString(com.squareup.leakcanary.R.string.leak_canary_notification_message);
        PendingIntent pendingIntent = DisplayLeakActivity.createPendingIntent(application, heapDump.referenceKey);
        showNotification(application, contentTitle, contentText, pendingIntent);
        allMemoryLeakInfo.add(new MemoryLeakInfo(heapDump, result, leakInfo));
        notifyObservers();
    }

    private String getContentTitle(AnalysisResult result) {
        String contentTitle;
        if (result.failure != null) {
            contentTitle = application.getString(com.squareup.leakcanary.R.string.leak_canary_analysis_failed);
        } else {
            String className = classSimpleName(result.className);
            if (result.leakFound) {
                if (result.retainedHeapSize == AnalysisResult.RETAINED_HEAP_SKIPPED) {
                    if (result.excludedLeak) {
                        contentTitle = application.getString(com.squareup.leakcanary.R.string.leak_canary_leak_excluded, className);
                    } else {
                        contentTitle = application.getString(com.squareup.leakcanary.R.string.leak_canary_class_has_leaked, className);
                    }
                } else {
                    String size = formatShortFileSize(application, result.retainedHeapSize);
                    if (result.excludedLeak) {
                        contentTitle =
                                application.getString(com.squareup.leakcanary.R.string.leak_canary_leak_excluded_retaining, className, size);
                    } else {
                        contentTitle =
                                application.getString(com.squareup.leakcanary.R.string.leak_canary_class_has_leaked_retaining, className, size);
                    }
                }
            } else {
                contentTitle = application.getString(com.squareup.leakcanary.R.string.leak_canary_class_no_leak, className);
            }
        }
        return contentTitle;
    }

    private static void showNotification(Context context, CharSequence contentTitle,
                                         CharSequence contentText, PendingIntent pendingIntent) {

        int notificationId = (int) (SystemClock.uptimeMillis() / 1000);
        createNotificationChannel(context);
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_monitor)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    private static void createNotificationChannel(Context context) {
        if (SDK_INT >= O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            if (notificationChannel == null) {
                String channelName = context.getString(R.string.appachhi_memory_leak);
                notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

}
