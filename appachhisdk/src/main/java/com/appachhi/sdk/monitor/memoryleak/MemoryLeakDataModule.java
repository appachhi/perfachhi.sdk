package com.appachhi.sdk.monitor.memoryleak;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import leakcanary.DefaultOnHeapAnalyzedListener;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import shark.HeapAnalysis;
import shark.HeapAnalysisSuccess;
import shark.Leak;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

@SuppressLint("PrivateResource")
public class MemoryLeakDataModule extends BaseDataModule<List<MemoryLeakInfo>> implements OnHeapAnalyzedListener {

    private static final String NOTIFICATION_CHANNEL_ID = "com.appachhi.sdk.memoryleak";
    public static final String TAG = "MemoryLeakDataModule";
    private List<MemoryLeakInfo> allMemoryLeakInfo = new ArrayList<>();
    private OnHeapAnalyzedListener defaultListener = DefaultOnHeapAnalyzedListener.Companion.create();
    private Application application;

    MemoryLeakDataModule(Application application) {
        this.application = application;
        LeakCanary.INSTANCE.showLeakDisplayActivityLauncherIcon(false);
        LeakCanary.Config existingConfig = LeakCanary.INSTANCE.getConfig();
        LeakCanary.Config newConfig = existingConfig.copy(
                existingConfig.getDumpHeap(),
                existingConfig.getDumpHeapWhenDebugging(),
                1,
                existingConfig.getReferenceMatchers(),
                existingConfig.getObjectInspectors(),
                this,
                existingConfig.getMetatadaExtractor(),
                existingConfig.getComputeRetainedHeapSize(),
                existingConfig.getMaxStoredHeapDumps(),
                existingConfig.getRequestWriteExternalStoragePermission(),
                existingConfig.getUseExperimentalLeakFinders()
        );
        LeakCanary.INSTANCE.setConfig(newConfig);
    }


    @Override
    protected List<MemoryLeakInfo> getData() {
        return allMemoryLeakInfo;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {
        if (heapAnalysis instanceof HeapAnalysisSuccess) {
            List<Leak> leaks = ((HeapAnalysisSuccess) heapAnalysis).getAllLeaks();
            List<MemoryLeakInfo> memoryLeakInfos = new ArrayList<>();
            Log.d(TAG,leaks.toString());
            for (Leak leak : leaks) {
                MemoryLeakInfo memoryLeakInfo = new MemoryLeakInfo(leak.getClassName(), leak.getLeakTrace());
                memoryLeakInfos.add(memoryLeakInfo);
                showNotification(application.getApplicationContext(), String.format("Class leaked : %s", leak.getClassName()));
            }
            this.allMemoryLeakInfo = memoryLeakInfos;
            if (!this.allMemoryLeakInfo.isEmpty()) {
                notifyObservers();
            }
        }
    }

    private static void showNotification(Context context, CharSequence message) {

        int notificationId = (int) (SystemClock.uptimeMillis() / 1000);
 //       createNotificationChannel(context);
//        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setContentTitle("Memory Leaked")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.ic_monitor)
//                .setWhen(System.currentTimeMillis())
//                .setOnlyAlertOnce(true)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notificationId, notification);
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
