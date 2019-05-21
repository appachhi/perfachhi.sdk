package com.appachhi.sdk.bugasura;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.JsonWriter;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.BaseEntity;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.TransitionStatEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BugaSuraDumpService extends IntentService {
    public static final String TAG = "BugaSuraDumpService";
    public static final int NOTIFICATION_ID = 123;
    public static final String NOTIFICATION_CHANNEL_ID = "Bugasura";
    private NotificationManager notificationManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BugaSuraDumpService() {
        super("BugaSuraDumpService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        startForeground(NOTIFICATION_ID, createNotification());
        AppachhiDB appachhiDB = Appachhi.getInstance().getDb();
        List<Session> sessions = appachhiDB.sessionDao().allSessions();
        List<CpuUsageEntity> cpuUsageEntities = appachhiDB.cpuUsageDao().allCpuUsage();
        List<FpsEntity> fpsEntities = appachhiDB.fpsDao().allFps();
        List<APICallEntity> apiCallEntities = appachhiDB.apiCallDao().allApiCalls();
        List<GCEntity> gcEntities = appachhiDB.gcDao().allGCRun();
        List<MemoryEntity> memoryEntities = appachhiDB.memoryDao().allMemoryUsage();
        List<MemoryLeakEntity> memoryLeakEntities = appachhiDB.memoryLeakDao().allMemoryLeak();
        List<MethodTraceEntity> methodTraceEntities = appachhiDB.methodTraceDao().allMethodTrace();
        List<NetworkUsageEntity> networkUsageEntities = appachhiDB.networkDao().allNetwork();
        List<TransitionStatEntity> transitionStatEntities = appachhiDB.screenTransitionDao().allScreenTransition();
        dumpJson(sessions, cpuUsageEntities, fpsEntities, apiCallEntities, gcEntities, memoryEntities,
                memoryLeakEntities, methodTraceEntities, networkUsageEntities, transitionStatEntities);
        Log.d(TAG, "Dump Service called");
        stopForeground(true);
        notifyComplete();
    }

    private void notifyComplete() {
        String completeAction = "com.appachhi.sdk.BUGASURA_DUMP_COMPLETE";
        String bugasuraPackageName = "com.appachhi.bugasura";
        Intent intent = new Intent(completeAction);
        intent.putExtra("PACKAGE_NAME", getPackageName());
        intent.setPackage(bugasuraPackageName);
        sendBroadcast(intent);
    }

    private Notification createNotification() {
        Log.d(TAG, "createNotification");
        createNotificationChannelIfNecessary();
        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Dump")
                .setContentText("Saving")
                .build();
    }

    private void createNotificationChannelIfNecessary() {
        Log.d(TAG, "createNotificationChannelIfNecessary");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Bugasura", NotificationManager.IMPORTANCE_MIN);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void dumpJson(List<Session> sessions, List<CpuUsageEntity> cpuUsageEntities,
                          List<FpsEntity> fpsEntities, List<APICallEntity> apiCallEntities,
                          List<GCEntity> gcEntities, List<MemoryEntity> memoryEntities,
                          List<MemoryLeakEntity> memoryLeakEntities,
                          List<MethodTraceEntity> methodTraceEntities,
                          List<NetworkUsageEntity> networkUsageEntities,
                          List<TransitionStatEntity> transitionStatEntities) {
        Log.d(TAG, "dumpJson");
        try {
            File folder = getExternalFilesDir("dumps");
            File dumpFile = new File(folder, "dump.json");
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(dumpFile));
            jsonWriter.setLenient(true);
            jsonWriter.beginObject();

            writeSession(jsonWriter, sessions);
            writeCpuUsage(jsonWriter, cpuUsageEntities);
            writeApiCall(jsonWriter, apiCallEntities);
            writeGcRun(jsonWriter, gcEntities);
            writeMemory(jsonWriter, memoryEntities);
            writeMemoryLeak(jsonWriter, memoryLeakEntities);
            writeMethodTrace(jsonWriter, methodTraceEntities);
            writeNetworkUsage(jsonWriter, networkUsageEntities);
            writeScreenTransition(jsonWriter, transitionStatEntities);
            writeFps(jsonWriter, fpsEntities);

            jsonWriter.endObject();
            jsonWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeScreenTransition(JsonWriter jsonWriter, List<TransitionStatEntity> transitionStatEntities) {
        Log.d(TAG, "writeScreenTransition");
        try {
            jsonWriter.name("screen_transition").beginArray();
            for (TransitionStatEntity transitionStatEntity : transitionStatEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, transitionStatEntity);
                jsonWriter.name("name").value(transitionStatEntity.getName());
                jsonWriter.name("duration").value(transitionStatEntity.getDuration());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNetworkUsage(JsonWriter jsonWriter, List<NetworkUsageEntity> networkUsageEntities) {
        Log.d(TAG, "writeNetworkUsage");
        try {
            jsonWriter.name("network_usage").beginArray();
            for (NetworkUsageEntity networkUsageEntity : networkUsageEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, networkUsageEntity);
                jsonWriter.name("send").value(networkUsageEntity.getDataSent());
                jsonWriter.name("received").value(networkUsageEntity.getDataReceived());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMethodTrace(JsonWriter jsonWriter, List<MethodTraceEntity> methodTraceEntities) {
        Log.d(TAG, "writeMethodTrace");
        try {
            jsonWriter.name("method_trace").beginArray();
            for (MethodTraceEntity methodTraceEntity : methodTraceEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, methodTraceEntity);
                jsonWriter.name("name").value(methodTraceEntity.getTraceName());
                jsonWriter.name("duration").value(methodTraceEntity.getDuration());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMemoryLeak(JsonWriter jsonWriter, List<MemoryLeakEntity> memoryLeakEntities) {
        Log.d(TAG, "writeMemoryLeak");
        try {
            jsonWriter.name("memory_leak").beginArray();
            for (MemoryLeakEntity memoryLeakEntity : memoryLeakEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, memoryLeakEntity);
                jsonWriter.name("class").value(memoryLeakEntity.getClassName());
                jsonWriter.name("leak_trace").value(memoryLeakEntity.getLeakTrace());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMemory(JsonWriter jsonWriter, List<MemoryEntity> memoryEntities) {
        Log.d(TAG, "writeMemory");
        try {
            jsonWriter.name("memory").beginArray();
            for (MemoryEntity memoryEntity : memoryEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, memoryEntity);
                jsonWriter.name("code").value(memoryEntity.getCode());
                jsonWriter.name("graphics").value(memoryEntity.getGraphics());
                jsonWriter.name("java_heap").value(memoryEntity.getJavaHeap());
                jsonWriter.name("native_heap").value(memoryEntity.getNativeHeap());
                jsonWriter.name("private_other").value(memoryEntity.getPrivateOther());
                jsonWriter.name("stack").value(memoryEntity.getStack());
                jsonWriter.name("swap").value(memoryEntity.getSwapMemory());
                jsonWriter.name("system").value(memoryEntity.getSystem());
                jsonWriter.name("system_resource").value(memoryEntity.getSystemResourceMemory());
                jsonWriter.name("threshold").value(memoryEntity.getThreshold());
                jsonWriter.name("private_dirty").value(memoryEntity.getTotalPrivateDirty());
                jsonWriter.name("total_pss").value(memoryEntity.getTotalPSS());
                jsonWriter.name("total_swap").value(memoryEntity.getTotalSwap());
                jsonWriter.name("total_private_dirty").value(memoryEntity.getTotalPrivateDirty());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeGcRun(JsonWriter jsonWriter, List<GCEntity> gcEntities) {
        Log.d(TAG, "writeGcRun");
        try {
            jsonWriter.name("gc").beginArray();
            for (GCEntity gcEntity : gcEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, gcEntity);
                jsonWriter.name("gc_name").value(gcEntity.getGcName());
                jsonWriter.name("alloc_space_object_freed").value(gcEntity.getAllocSpaceObjectFreed());
                jsonWriter.name("alloc_space_object_freed_size").value(gcEntity.getAllocSpaceObjectFreedSize());
                jsonWriter.name("pause_time").value(gcEntity.getGcPauseTime());
                jsonWriter.name("reason").value(gcEntity.getGcReason());
                jsonWriter.name("runtime").value(gcEntity.getGcRunTime());
                jsonWriter.name("large_object_freed_percentage").value(gcEntity.getLargeObjectFreedPercentage());
                jsonWriter.name("large_object_freed_size").value(gcEntity.getLargeObjectFreedSize());
                jsonWriter.name("large_object_total_size").value(gcEntity.getLargeObjectTotalSize());
                jsonWriter.name("object_freed").value(gcEntity.getObjectFreed());
                jsonWriter.name("object_freed_size").value(gcEntity.getObjectFreedSize());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeApiCall(JsonWriter jsonWriter, List<APICallEntity> apiCallEntities) {
        Log.d(TAG, "writeApiCall");
        try {
            jsonWriter.name("api_calls").beginArray();
            for (APICallEntity apiCallEntity : apiCallEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, apiCallEntity);
                jsonWriter.name("url").value(apiCallEntity.getUrl());
                jsonWriter.name("content_type").value(apiCallEntity.getContentType());
                jsonWriter.name("duration").value(apiCallEntity.getDuration());
                jsonWriter.name("method_type").value(apiCallEntity.getMethodType());
                jsonWriter.name("request_content_length").value(apiCallEntity.getRequestContentLength());
                jsonWriter.name("response_code").value(apiCallEntity.getResponseCode());
                jsonWriter.name("thread_name").value(apiCallEntity.getThreadName());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFps(JsonWriter jsonWriter, List<FpsEntity> fpsEntities) {
        Log.d(TAG, "writeFps");
        try {
            jsonWriter.name("fps").beginArray();
            for (FpsEntity fpsEntity : fpsEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, fpsEntity);
                jsonWriter.name("fps").value(fpsEntity.getFps());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeCpuUsage(JsonWriter jsonWriter, List<CpuUsageEntity> cpuUsageEntities) {
        Log.d(TAG, "writeCpuUsage");
        try {
            jsonWriter.name("cpu_usages").beginArray();
            for (CpuUsageEntity cpuUsageEntity : cpuUsageEntities) {
                jsonWriter.beginObject();
                writeBaseEntity(jsonWriter, cpuUsageEntity);
                jsonWriter.name("appCpuUsage").value(cpuUsageEntity.getAppCpuUsage());
                jsonWriter.name("deviceCpuUsage").value(cpuUsageEntity.getDeviceCpuUsage());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBaseEntity(JsonWriter jsonWriter, Object baseEntity) throws IOException {
        @SuppressWarnings("unchecked")
        BaseEntity entity = (BaseEntity) baseEntity;
        jsonWriter.name("id").value(entity.getId());
        jsonWriter.name("sessionId").value(entity.getSessionId());
        jsonWriter.name("sessionTime").value(entity.getSessionTime());
        jsonWriter.name("executionTime").value(entity.getExecutionTime());

    }

    private void writeSession(JsonWriter jsonWriter, List<Session> sessions) {
        Log.d(TAG, "writeSession");
        try {
            jsonWriter.name("sessions").beginArray();
            for (Session session : sessions) {
                jsonWriter.beginObject();
                jsonWriter.name("id").value(session.getId());
                jsonWriter.name("startTime").value(session.getStartTime());
                jsonWriter.name("model").value(session.getModel());
                jsonWriter.name("manufacturer").value(session.getManufacturer());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
