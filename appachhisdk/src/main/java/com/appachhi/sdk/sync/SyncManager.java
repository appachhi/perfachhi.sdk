package com.appachhi.sdk.sync;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.dao.FrameDropDao;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.database.dao.LogsDao;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.database.dao.MethodTraceDao;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.dao.ScreenshotDao;
import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.BaseEntity;
import com.appachhi.sdk.database.entity.BaseFileEntity;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.FrameDropEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.LogsEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.ScreenshotEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private static final String BASE_URL = "https://57612d87.ngrok.io";
    private static String KEY = null;
    private Gson gson;
    private ScheduledExecutorService syncExecutor;
    private ExecutorService metricSyncExecutor;
    private SharedPreferences appachhiPref;

    private SyncManager(SharedPreferences appachhiPref) {
        this.appachhiPref = appachhiPref;
        this.appachhiDB = Appachhi.getInstance().getDb();
        this.gson = new GsonBuilder()
                .create();
        syncExecutor = Executors.newSingleThreadScheduledExecutor();
        metricSyncExecutor = Executors.newCachedThreadPool();
    }

    public static SyncManager create(Application application) {
        loadApiKey(application);
        return new SyncManager(application.getSharedPreferences("appachhi_pref", Context.MODE_PRIVATE));
    }

    private static void loadApiKey(Application application) {
        try {
            ApplicationInfo info = ((Context) application).getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            if (info.metaData == null) {
                Log.w(TAG, "Perfachhi api key is missing");
                return;
            }
            KEY = info.metaData.getString("perfachhi_api_key", null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startSync() {
        if (KEY == null) {
            Log.e("Appachhi", "API key is null. Cannot start sync");
        }
        Log.d(TAG, "startSync");
        if (syncExecutor.isShutdown()) {
            syncExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        syncExecutor.scheduleAtFixedRate(scheduleSyncRunnable, 10, 20, TimeUnit.SECONDS);
    }

    public void stopSync() {
        if (!syncExecutor.isShutdown()) {
            syncExecutor.shutdown();
        }
    }

    private void uploadAllMetric() {
        Log.d(TAG, "Upload All Metric");
        uploadDeviceDetails();
        if (!isDeviceDetailUploaded()) {
            Log.d(TAG, "Device Detail not uploaded yet");
            // Don't Proceed if the device is not synced already
            return;
        }
        // Proceed to uploading session only when the device detail is uploaded
        uploadSessions(appachhiDB.sessionDao());

        // Fetch all the synced session ids
        List<String> allSyncedSessionIds = appachhiDB.sessionDao().allSyncedSessionIds();

        // Upload Screen Transition for all the synced session only
        uploadScreenTransitionForSession(allSyncedSessionIds, appachhiDB.screenTransitionDao());

        // Upload CpuUsage for all the synced session only
        uploadCpuUsage(allSyncedSessionIds, appachhiDB.cpuUsageDao());

        // Upload FPS for all the synced session only
        uploadFps(allSyncedSessionIds, appachhiDB.fpsDao());

        // Upload GC for all the synced session only
        uploadGc(allSyncedSessionIds, appachhiDB.gcDao());

        // Upload Memory for all the synced session only
        uploadMemory(allSyncedSessionIds, appachhiDB.memoryDao());

        // Upload Memory Leak for all the synced session only
        uploadMemoryLeak(allSyncedSessionIds, appachhiDB.memoryLeakDao());

        // Upload Method Trace for all the synced session only
        uploadMethodTrace(allSyncedSessionIds, appachhiDB.methodTraceDao());

        // Upload Network Usage for all the synced session only
        uploadNetworkUsage(allSyncedSessionIds, appachhiDB.networkDao());


        // Upload Frame Drop for all the synced session only
        uploadFrameDrop(allSyncedSessionIds, appachhiDB.frameDropDao());


        // Upload Api Call for all the synced session only
        uploadNetworkCall(allSyncedSessionIds, appachhiDB.apiCallDao());

        // Upload Screenshot for all synced session only

        uploadScreenShot(allSyncedSessionIds, appachhiDB.screenshotDao());

        uploadLogs(allSyncedSessionIds, appachhiDB.logsDao());
    }

    private void uploadScreenShot(List<String> allSyncedSessionIds, final ScreenshotDao screenshotDao) {
        List<ScreenshotEntity> screenshotEntities = screenshotDao.unSyncedScreenshotEntityForSession(allSyncedSessionIds, 20);
        uploadFile("screenshot", "screenshot", screenshotEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                screenshotDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadLogs(List<String> allSyncedSessionIds, final LogsDao logsDao) {
        List<LogsEntity> logsEntities = logsDao.unSyncedLogEntityForSession(allSyncedSessionIds, 5);
        uploadFile("logs", "logs", logsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                logsDao.updateSuccessSyncStatus(ids);
            }
        });
    }


    private void uploadNetworkCall(List<String> sessionIds, final APICallDao apiCallDao) {
        List<APICallEntity> fpsEntities = apiCallDao.allUnSyncedApiCallEntityForSession(sessionIds);
        uploadMetric("network_call", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                apiCallDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadFrameDrop(List<String> allSyncedSessionIds, final FrameDropDao frameDropDao) {
        List<FrameDropEntity> fpsEntities = frameDropDao.allUnSyncedFpsEntityForSession(allSyncedSessionIds);
        uploadMetric("frame_drop", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                frameDropDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadNetworkUsage(List<String> sessionIds, final NetworkDao networkDao) {
        List<NetworkUsageEntity> fpsEntities = networkDao.allUnSyncedNetworkUsageEntityForSession(sessionIds);
        uploadMetric("network_usage", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                networkDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMethodTrace(List<String> sessionIds, final MethodTraceDao methodTraceDao) {
        List<MethodTraceEntity> fpsEntities = methodTraceDao.allUnSyncedMethodTraceEntityForSession(sessionIds);
        uploadMetric("method_trace", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                methodTraceDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMemoryLeak(List<String> sessionIds, final MemoryLeakDao memoryLeakDao) {
        List<MemoryLeakEntity> fpsEntities = memoryLeakDao.allUnSyncedMemoryLeakEntityForSession(sessionIds);
        uploadMetric("memory_leak", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                memoryLeakDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMemory(List<String> sessionIds, final MemoryDao memoryDao) {
        List<MemoryEntity> fpsEntities = memoryDao.allUnSyncedMemoryEntityForSession(sessionIds);
        uploadMetric("memory_usage", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                memoryDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadGc(List<String> sessionIds, final GCDao gcDao) {
        List<GCEntity> gcEntities = gcDao.allUnSyncedGcEntityForSession(sessionIds);

        uploadMetric("gc", gcEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                gcDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadCpuUsage(List<String> sessionIds, final CpuUsageDao cpuUsageDao) {
        List<CpuUsageEntity> cpuUsageEntities = cpuUsageDao.allUnSyncedCpuEntityForSession(sessionIds);
        uploadMetric("cpu_usage", cpuUsageEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                cpuUsageDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadFps(List<String> sessionIds, final FpsDao fpsDao) {
        List<FpsEntity> fpsEntities = fpsDao.allUnSyncedFpsEntityForSession(sessionIds);
        uploadMetric("fps", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                fpsDao.updateSuccessSyncStatus(ids);
            }
        });

    }

    /**
     * Upload the screen transition status for all the give session only
     */
    private void uploadScreenTransitionForSession(List<String> sessionIds, final ScreenTransitionDao screenTransitionDao) {

        List<TransitionStatEntity> transitionStatEntities = screenTransitionDao.allUnSyncedScreenTransitionForSession(sessionIds);
        uploadMetric("screen_transition", transitionStatEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids) {
                screenTransitionDao.updateSuccessSyncStatus(ids);
            }
        });

    }

    private void uploadMetric(final String path,
                              final List<? extends BaseEntity> items,
                              final OnMetricUploadListener listener) {
        if (items.isEmpty()) {
            Log.d(TAG, String.format("No %s to upload", path));
            return;
        }
        Log.d(TAG, "Upload " + path);
        metricSyncExecutor.execute(new Runnable() {
            @Override
            public void run() {
                JsonElement element = gson.toJsonTree(items);
                JsonArray array = addDeviceIdProperty(element);
                String jsonArray = gson.toJson(array);
                Request request = getRequest(path, jsonArray);
                try {
                    Response response = getClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.d(TAG, String.format("%s uploaded", path));
                        List<String> ids = new ArrayList<>();
                        for (BaseEntity entity : items) {
                            ids.add(entity.getId());
                        }
                        listener.onMetricUpload(ids);
                    } else {
                        Log.d(TAG, String.format("%s upload failed with error : %s", path, response.message()));
                    }
                } catch (IOException e) {
                    Log.e(TAG, String.format("Failed to upload %s", path), e);
                }
            }
        });
    }

    private void uploadFile(final String path,
                            final String fileKey,
                            final List<? extends BaseFileEntity> items,
                            final OnMetricUploadListener listener) {
        if (items.isEmpty()) {
            Log.d(TAG, String.format("No %s to upload", path));
            return;
        }
        Log.d(TAG, "Upload " + path);
        metricSyncExecutor.execute(new Runnable() {
            @Override
            public void run() {
                JsonElement element = gson.toJsonTree(items);
                List<String> filePaths = new ArrayList<>();
                for (BaseFileEntity item : items) {
                    filePaths.add(item.getFilePath());
                }
                JsonArray array = addDeviceIdProperty(element);
                String jsonArray = gson.toJson(array);
                Request request = getFileUploadRequest(path, fileKey, filePaths, jsonArray, items.get(0).getMimeType());
                try {
                    Response response = getClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.d(TAG, String.format("%s uploaded", path));
                        List<String> ids = new ArrayList<>();
                        for (BaseEntity entity : items) {
                            ids.add(entity.getId());
                        }
                        listener.onMetricUpload(ids);
                    } else {
                        Log.d(TAG, String.format("%s upload failed with error : %s", path, response.message()));
                    }
                } catch (IOException e) {
                    Log.e(TAG, String.format("Failed to upload %s", path), e);
                }
            }
        });
    }

    private Request getRequest(String path, String contentArray) {
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), contentArray);
        return new Request.Builder()
                .url(String.format("%s/%s", BASE_URL, path))
                .post(requestBody)
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
    }

    private Request getFileUploadRequest(String path, String fileKey, List<String> filePaths, String contentArray, String mimeType) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", contentArray);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            RequestBody body = RequestBody.create(MediaType.get(mimeType), file);
            String fileName = Uri.fromFile(file).getLastPathSegment();
            builder.addFormDataPart(fileKey, fileName, body);
        }
        return new Request.Builder()
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .url(String.format("%s/%s", BASE_URL, path))
                .post(builder.build())
                .build();
    }

    /**
     * Uploads all the unsynced session and set the sync status to success
     */
    private void uploadSessions(SessionDao sessionDao) {
        Log.d(TAG, "Upload Sessions");
        // Fetch All the un synced session
        List<Session> sessions = sessionDao.allUnSyncedSessions();
        if (sessions.isEmpty()) {
            Log.d(TAG, "No session to upload");
            return;
        }
        JsonArray jsonArray = addDeviceIdProperty(gson.toJsonTree(sessions));
        Request request = getRequest("session", gson.toJson(jsonArray));
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

    private JsonArray addDeviceIdProperty(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        for (JsonElement jsonElement : array) {
            jsonElement.getAsJsonObject().addProperty("deviceId", getDeviceId());
        }
        return array;
    }

    private OkHttpClient getClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(600, TimeUnit.SECONDS)
                    .connectTimeout(600, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    private static final String DEVICE_ID_KEY = "device_id";
    private static final String DEVICE_ID_UPLOADED = "device_id_uploaded";

    private boolean isDeviceDetailUploaded() {
        return appachhiPref.getBoolean(DEVICE_ID_UPLOADED, false);
    }

    private void deviceIDUploaded() {
        appachhiPref.edit().putBoolean(DEVICE_ID_UPLOADED, true).apply();
    }

    private String getDeviceId() {
        String storedDeviceId = appachhiPref.getString(DEVICE_ID_KEY, null);
        if (storedDeviceId == null) {
            storedDeviceId = UUID.randomUUID().toString();
            appachhiPref.edit().putString(DEVICE_ID_KEY, storedDeviceId).apply();
        }
        return storedDeviceId;
    }

    /**
     * Upload the device detail if not uploaded already.Repeated upload will return in
     * unsuccessful result from the api
     * <p>
     * Once uploaded , sync status is turned to true
     */
    private void uploadDeviceDetails() {
        if (isDeviceDetailUploaded()) {
            return;
        }
        try {
            Log.d(TAG, "Uploading device details");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", getDeviceId());
            jsonObject.put("manufacturer", Build.BRAND);
            jsonObject.put("model", Build.MODEL);
            jsonObject.put("os", "android");
            jsonObject.put("osVersion", Build.VERSION.SDK_INT);
            jsonArray.put(jsonObject);

            Request request = getRequest("device", jsonArray.toString());
            Log.d(TAG, String.format("Url is %s", request.url().toString()));
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                deviceIDUploaded();
            } else {
                Log.d(TAG, String.format("Failed to upload device details : %s", response.message()));
            }

        } catch (JSONException | IOException e) {
            Log.e(TAG, "Failed to upload device details", e);
        }

    }

    interface OnMetricUploadListener {
        void onMetricUpload(List<String> ids);
    }
}
