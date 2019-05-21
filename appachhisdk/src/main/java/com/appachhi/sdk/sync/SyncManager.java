package com.appachhi.sdk.sync;

import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.database.dao.MethodTraceDao;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SyncManager {
    private final Runnable scheduleSyncRunnable = new Runnable() {
        @Override
        public void run() {
            uploadAllMetric();
        }
    };
    public static final String TAG = "SyncManager";
    private AppachhiDB appachhiDB;
    private OkHttpClient okHttpClient;
    private static final String BASE_URL = "https://perfachhi.appspot.com";
    private static final String KEY = "61bce6f5a3e9b02dd810ce718b40d25c";
    private Gson gson;
    private ScheduledExecutorService syncExecutor;

    private SyncManager() {
        this.appachhiDB = Appachhi.getInstance().getDb();
        this.gson = new GsonBuilder()
                .create();
        syncExecutor = Executors.newScheduledThreadPool(1);
    }

    public static SyncManager create() {
        return new SyncManager();
    }

    public void startSync() {
        Log.d(TAG, "startSync");
        if (syncExecutor.isShutdown()) {
            syncExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        syncExecutor.scheduleAtFixedRate(scheduleSyncRunnable, 10, 10, TimeUnit.SECONDS);
    }

    public void stopSync() {
        if (!syncExecutor.isShutdown()) {
            syncExecutor.shutdown();
        }
    }

    private void uploadAllMetric() {
        Log.d(TAG, "Upload All Metric");
        uploadSessions(appachhiDB.sessionDao());
        uploadScreenTransition(appachhiDB.screenTransitionDao());
        uploadCpuUsage(appachhiDB.cpuUsageDao());
        uploadFps(appachhiDB.fpsDao());
        uploadGc(appachhiDB.gcDao());
        uploadMemory(appachhiDB.memoryDao());
        uploadMemoryLeak(appachhiDB.memoryLeakDao());
        uploadMethodTrace(appachhiDB.methodTraceDao());
        uploadNetworkUsage(appachhiDB.networkDao());
        uploadNetworkCall(appachhiDB.apiCallDao());
    }

    private void uploadNetworkCall(APICallDao apiCallDao) {
        List<APICallEntity> apiCallEntities = apiCallDao.oldest200UnSyncedNetworkUsage();
        Log.d(TAG, String.format("%d network call fetched", apiCallEntities.size()));
        if (apiCallEntities.isEmpty()) {
            Log.d(TAG, "No network call to upload");
            return;
        }

        String jsonArray = gson.toJson(apiCallEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/network_call", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Network call uploaded");
                List<String> ids = new ArrayList<>();
                for (APICallEntity apiCallEntity: apiCallEntities) {
                    ids.add(apiCallEntity.getId());
                }
                apiCallDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Network call upload failed with error : %d", response.code()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload network call", e);
        }
    }

    private void uploadNetworkUsage(NetworkDao networkDao) {
        List<NetworkUsageEntity> networkUsageEntities = networkDao.oldest200UnSyncedNetworkUsage();
        Log.d(TAG, String.format("%d network Usage fetched", networkUsageEntities.size()));
        if (networkUsageEntities.isEmpty()) {
            Log.d(TAG, "No network Usage to upload");
            return;
        }

        String jsonArray = gson.toJson(networkUsageEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/network_usage", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Network Usage uploaded");
                List<String> ids = new ArrayList<>();
                for (NetworkUsageEntity networkUsageEntity: networkUsageEntities) {
                    ids.add(networkUsageEntity.getId());
                }
                networkDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Network Usage upload failed with error : %d", response.code()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload network usage", e);
        }
    }

    private void uploadMethodTrace(MethodTraceDao methodTraceDao) {
        List<MethodTraceEntity> methodTraceEntities = methodTraceDao.oldest200UnSyncedMethodTrace();
        Log.d(TAG, String.format("%d memory trace fetched", methodTraceEntities.size()));
        if (methodTraceEntities.isEmpty()) {
            Log.d(TAG, "No memory trace to upload");
            return;
        }

        String jsonArray = gson.toJson(methodTraceEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/method_trace", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Memory Trace uploaded");
                List<String> ids = new ArrayList<>();
                for (MethodTraceEntity methodTraceEntity: methodTraceEntities) {
                    ids.add(methodTraceEntity.getId());
                }
                methodTraceDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Memory trace upload failed with error : %d", response.code()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload memory trace", e);
        }
    }

    private void uploadMemoryLeak(MemoryLeakDao memoryLeakDao) {
        List<MemoryLeakEntity> memoryLeakEntities = memoryLeakDao.oldest200UnSyncedMemoryLeak();
        Log.d(TAG, String.format("%d memory leak fetched", memoryLeakEntities.size()));
        if (memoryLeakEntities.isEmpty()) {
            Log.d(TAG, "No memory leak to upload");
            return;
        }

        String jsonArray = gson.toJson(memoryLeakEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/memory_leak", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Memory leak uploaded");
                List<String> ids = new ArrayList<>();
                for (MemoryLeakEntity memoryLeakEntity : memoryLeakEntities) {
                    ids.add(memoryLeakEntity.getId());
                }
                memoryLeakDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Memory leak upload failed with error : %d", response.code()));
                Log.d(TAG,memoryLeakEntities.get(0).getLeakTrace());
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload memory leak", e);
        }
    }

    private void uploadMemory(MemoryDao memoryDao) {
        // Fetch oldest 2000 UnSynced session
        List<MemoryEntity> memoryEntities = memoryDao.oldest200UnSyncedMemory();
        Log.d(TAG, String.format("%d memory usage fetched", memoryEntities.size()));
        if (memoryEntities.isEmpty()) {
            Log.d(TAG, "No memory to upload");
            return;
        }

        String jsonArray = gson.toJson(memoryEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/memory_usage", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Memory usage uploaded");
                List<String> ids = new ArrayList<>();
                for (MemoryEntity memoryEntity : memoryEntities) {
                    ids.add(memoryEntity.getId());
                }
                memoryDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Memory Usage upload failed with error : %d", response.code()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload memory usage", e);
        }
    }

    private void uploadGc(GCDao gcDao) {
        // Fetch oldest 2000 UnSynced session
        List<GCEntity> gcEntities = gcDao.oldest200UnSyncedGc();
        Log.d(TAG, String.format("%d gc fetched", gcEntities.size()));
        if (gcEntities.isEmpty()) {
            Log.d(TAG, "No gc to upload");
            return;
        }

        String jsonArray = gson.toJson(gcEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/gc", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "GC uploaded");
                List<String> ids = new ArrayList<>();
                for (GCEntity gcEntity : gcEntities) {
                    ids.add(gcEntity.getId());
                }
                gcDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Gc upload failed with error : %s", response.message()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload gc", e);
        }
    }

    private void uploadCpuUsage(CpuUsageDao cpuUsageDao) {
        // Fetch oldest 2000 UnSynced session
        List<CpuUsageEntity> cpuUsageEntities = cpuUsageDao.oldest200UnSyncedCpuUsages();
        Log.d(TAG, String.format("%d cpu usage fetched", cpuUsageEntities.size()));
        if (cpuUsageEntities.isEmpty()) {
            Log.d(TAG, "No cpu usage to upload");
            return;
        }

        String jsonArray = gson.toJson(cpuUsageEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/cpu_usage", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Cpu Usages uploaded");
                List<String> ids = new ArrayList<>();
                for (CpuUsageEntity cpuUsageEntity : cpuUsageEntities) {
                    ids.add(cpuUsageEntity.getId());
                }
                cpuUsageDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Cpu Usage upload failed with error : %s", response.message()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload cpu usage", e);
        }
    }

    private void uploadFps(FpsDao fpsDao) {
        // Fetch oldest 2000 UnSynced fps
        List<FpsEntity> fpsEntities = fpsDao.oldest200UnSyncedFps();
        Log.d(TAG, String.format("%d fps fetched", fpsEntities.size()));
        if (fpsEntities.isEmpty()) {
            Log.d(TAG, "No fps to upload");
            return;
        }

        String jsonArray = gson.toJson(fpsEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/fps", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Fps uploaded");
                List<String> ids = new ArrayList<>();
                for (FpsEntity fpsEntity : fpsEntities) {
                    ids.add(fpsEntity.getId());
                }
                fpsDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Fps upload failed with error : %s with status %d", response.message(), response.code()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload fps", e);
        }
    }

    private void uploadScreenTransition(ScreenTransitionDao screenTransitionDao) {
        Log.d(TAG, "upload Screen Transition Stat");
        // Fetch oldest 2000 UnSynced session
        List<TransitionStatEntity> transitionStatEntities = screenTransitionDao.oldest200UnSyncedTransitionStat();
        Log.d(TAG, String.format("%d transition state fetched", transitionStatEntities.size()));
        if (transitionStatEntities.isEmpty()) {
            Log.d(TAG, "No screen transition to upload");
            return;
        }

        String jsonArray = gson.toJson(transitionStatEntities);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/screen_transition", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Screen Transition Stat uploaded");
                List<String> ids = new ArrayList<>();
                for (TransitionStatEntity transitionStatEntity : transitionStatEntities) {
                    ids.add(transitionStatEntity.getId());
                }
                screenTransitionDao.updateSuccessSyncStatus(ids);
            } else {
                Log.d(TAG, String.format("Screen Transition Stat upload failed with error : %s", response.message()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload screen transition", e);
        }
    }

    private void uploadSessions(SessionDao sessionDao) {
        Log.d(TAG, "Upload Sessions");
        // Fetch oldest 2000 UnSynced session
        List<Session> sessions = sessionDao.oldest200UnSyncedSessions();
        Log.d(TAG, String.format("%d session fetched", sessions.size()));
        if (sessions.isEmpty()) {
            Log.d(TAG, "No session to upload");
            return;
        }

        String jsonArray = gson.toJson(sessions);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonArray);
        Request request = new Request.Builder().url(String.format("%s/session", BASE_URL))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Session Uploaded");
                List<String> sessionIds = new ArrayList<>();
                for (Session session : sessions) {
                    sessionIds.add(session.getId());
                }
                sessionDao.updateSuccessSyncStatus(sessionIds);
            } else {
                Log.d(TAG, String.format("Session upload failed with error : %s", response.message()));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to upload sessions", e);
        }
    }

    private OkHttpClient getClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
