package com.appachhi.sdk.sync;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.dao.BatteryDataDao;
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
import com.appachhi.sdk.database.dao.StartupDao;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.BaseEntity;
import com.appachhi.sdk.database.entity.BaseFileEntity;
import com.appachhi.sdk.database.entity.BatteryEntity;
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
import com.appachhi.sdk.database.entity.StartupEntity;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.appachhi.sdk.instrument.sdkfeatures.PerfachhiConfig;
import com.appachhi.sdk.monitor.battery.BatteryBasicDetails;
import com.appachhi.sdk.monitor.battery.BatteryDataObject;
import com.appachhi.sdk.monitor.battery.DataListener;
import com.appachhi.sdk.monitor.devicedetails.DeviceDataObject;
import com.appachhi.sdk.monitor.devicedetails.DeviceDetailUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    public static final String TAG = "SyncManager";
    private static final String BASE_URL = "https://perfachhi.appspot.com";
    //private static final String BASE_URL = "https://1846690d06fe.ngrok.io";

    private static final String DEVICE_ID_KEY = "device_id";
    private static final String SECURE_ID_KEY = "secure_id";

    private static final String DEVICE_ID_UPLOADED = "device_id_uploaded";
    private static final String CONFIG_FETCHED = "device_id_uploaded";
    private static String KEY = null;
    private AppachhiDB appachhiDB;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private ScheduledExecutorService syncExecutor;
    private ExecutorService metricSyncExecutor;
    private SharedPreferences appachhiPref;
    SharedPreferences.Editor appachhiPrefEditor;

    public static PerfachhiConfig perfachhiConfig;

    private final Runnable scheduleSyncRunnable = new Runnable() {
        @Override
        public void run() {
            uploadAllMetric();
        }
    };


    public static DeviceDetailUtils deviceDetailUtils;
    public static DeviceDataObject deviceDataObject;



    private SyncManager(SharedPreferences appachhiPref) {
        this.appachhiPref = appachhiPref;
        this.appachhiDB = Appachhi.getInstance().getDb();
        this.gson = new GsonBuilder()
                .create();
        syncExecutor = Executors.newSingleThreadScheduledExecutor();
        metricSyncExecutor = Executors.newCachedThreadPool();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static SyncManager create(Application application) {
        loadApiKey(application);

        deviceDetailUtils = new DeviceDetailUtils();
        deviceDataObject = deviceDetailUtils.fetchDeviceData(application.getBaseContext());


        perfachhiConfig = new PerfachhiConfig(application.getApplicationContext());

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
    //Trigger metrics

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

        checkConfigStats();

        uploadDeviceDetails();
        if (!isDeviceDetailUploaded()) {
            Log.d(TAG, "Device Detail not uploaded yet");
            // Don't Proceed if the device is not synced already
            return;
        }



       /* try {
            checkConfigStats();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //If device is uploaded, then you start uploading all the sessions.
        /*if (isDeviceDetailUploaded()) {*/
        // Proceed to uploading session only when the device detail is uploaded
        Log.d(TAG, "uploadAllMetric: isDeviceDetailUpload : " + isDeviceDetailUploaded());
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

        uploadStartupTime(allSyncedSessionIds, appachhiDB.startupDao());

        uploadBatteryStats(allSyncedSessionIds, appachhiDB.batteryDataDao());
        // Check flag f

        //}

    }


    private void uploadScreenShot(List<String> allSyncedSessionIds, final ScreenshotDao screenshotDao) {
        List<ScreenshotEntity> screenshotEntities = screenshotDao.unSyncedScreenshotEntityForSession(allSyncedSessionIds, 20);
        uploadFile("screenshot", "screenshot", screenshotEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                screenshotDao.updateSuccessSyncStatus(ids);

                for (int i = 0; i < filepaths.size(); i++) {
                    deleteImage(filepaths.get(i));
                    Log.d(TAG, "onMetricUpload: From upload screenshot : " + filepaths.get(i));

                }
            }
        });
    }

    private void deleteImage(String itemPath) {
        File file = new File(itemPath);
        boolean deleted = file.delete();
        Log.d(TAG, "deleteImage: Status : " + deleted);
    }

    private void uploadLogs(List<String> allSyncedSessionIds, final LogsDao logsDao) {
        List<LogsEntity> logsEntities = logsDao.unSyncedLogEntityForSession(allSyncedSessionIds, 5);
        uploadFile("logs", "logs", logsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                logsDao.updateSuccessSyncStatus(ids);

                for (int i = 0; i < filepaths.size(); i++) {
                    deleteImage(filepaths.get(i));
                    Log.d(TAG, "onMetricUpload: From upload logs : " + filepaths.get(i));

                }
            }
        });
    }

    private void uploadNetworkCall(List<String> sessionIds, final APICallDao apiCallDao) {
        List<APICallEntity> apiEntities = apiCallDao.allUnSyncedApiCallEntityForSession(sessionIds);
        uploadMetric("network_call", apiEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                apiCallDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadFrameDrop(List<String> allSyncedSessionIds, final FrameDropDao frameDropDao) {
        List<FrameDropEntity> fpsEntities = frameDropDao.allUnSyncedFpsEntityForSession(allSyncedSessionIds);
        uploadMetric("frame_drop", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                frameDropDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadNetworkUsage(List<String> sessionIds, final NetworkDao networkDao) {
        List<NetworkUsageEntity> fpsEntities = networkDao.allUnSyncedNetworkUsageEntityForSession(sessionIds);
        uploadMetric("network_usage", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                networkDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMethodTrace(List<String> sessionIds, final MethodTraceDao methodTraceDao) {
        List<MethodTraceEntity> methodTraceEntities = methodTraceDao.allUnSyncedMethodTraceEntityForSession(sessionIds);
        uploadMetric("method_trace", methodTraceEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                methodTraceDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMemoryLeak(List<String> sessionIds, final MemoryLeakDao memoryLeakDao) {
        List<MemoryLeakEntity> fpsEntities = memoryLeakDao.allUnSyncedMemoryLeakEntityForSession(sessionIds);
        uploadMetric("memory_leak", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                memoryLeakDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadMemory(List<String> sessionIds, final MemoryDao memoryDao) {
        List<MemoryEntity> fpsEntities = memoryDao.allUnSyncedMemoryEntityForSession(sessionIds);
        uploadMetric("memory_usage", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                memoryDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadGc(List<String> sessionIds, final GCDao gcDao) {
        List<GCEntity> gcEntities = gcDao.allUnSyncedGcEntityForSession(sessionIds);

        uploadMetric("gc", gcEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                gcDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadCpuUsage(List<String> sessionIds, final CpuUsageDao cpuUsageDao) {
        List<CpuUsageEntity> cpuUsageEntities = cpuUsageDao.allUnSyncedCpuEntityForSession(sessionIds);
        uploadMetric("cpu_usage", cpuUsageEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                cpuUsageDao.updateSuccessSyncStatus(ids);
            }
        });
    }

    private void uploadFps(List<String> sessionIds, final FpsDao fpsDao) {
        Log.d(TAG, "uploadFps: " + sessionIds.get(0));
        List<FpsEntity> fpsEntities = fpsDao.allUnSyncedFpsEntityForSession(sessionIds);
        uploadMetric("fps", fpsEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                fpsDao.updateSuccessSyncStatus(ids);
            }
        });

    }

    private void uploadStartupTime(List<String> sessionIds, final StartupDao startupDao) {
        List<StartupEntity> startupEntities = startupDao.allUnSyncedStartupEntityForSession(sessionIds);
        uploadMetric("startup_time", startupEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                startupDao.updateSucessSyncStatus(ids);
            }
        });
    }

    private void uploadBatteryStats(List<String> sessionIds, final BatteryDataDao batteryDataDao) {
        List<BatteryEntity> batteryEntities = batteryDataDao.allUnSyncedBatteryEntityForSession(sessionIds);
        uploadMetric("battery_stats", batteryEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                Log.d(TAG, "onMetricUpload: Battery Data IDS : " + ids + "\n");
                batteryDataDao.updateSucessSyncStatus(ids);
            }
        });
    }

   /* private void uploadStartupTime(List<String> sessionIds, final StartupDao startupDao) {
        Log.d(TAG, "uploadStartupTime: Entered here. " + sessionIds.get(1));
        List<StartupEntity> startupEntities = startupDao.allUnSyncedStartupEntityForSession(sessionIds);
        uploadMetric("startup_time", startupEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
                Log.d(TAG, "on: Entered uploadStartupTime");
                startupDao.updateSucessSyncStatus(ids);
            }
        });
    }
*/

    /**
     * Upload the screen transition status for all the give session only
     */
    private void uploadScreenTransitionForSession(List<String> sessionIds, final ScreenTransitionDao screenTransitionDao) {

        List<TransitionStatEntity> transitionStatEntities = screenTransitionDao.allUnSyncedScreenTransitionForSession(sessionIds);
        uploadMetric("screen_transition", transitionStatEntities, new OnMetricUploadListener() {
            @Override
            public void onMetricUpload(List<String> ids, List<String> filepaths) {
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

                List<String> ids = new ArrayList<>();
                for (BaseEntity entity : items) {
                    ids.add(entity.getId());

                }

             /*   if (TextUtils.equals(path, "battery_stats")) {
                    Log.d(TAG, "run: JSON : " + jsonArray);
                }
*/

                List<String> filepaths = new ArrayList<>();

                try {
                    Response response = getClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "run: Response Code : " + response.code() + " : " + response.message());
                        Log.d(TAG, String.format("%s uploaded", path));
                        listener.onMetricUpload(ids, filepaths);
                    }
                    if (!response.isSuccessful() && response.code() == 500) {
                        Log.d(TAG, "run: Updating sync status to 1");
                        listener.onMetricUpload(ids, filepaths);
                        Log.d(TAG, "run: Response Code : " + response.body().string() + ": Response message : " + response.message());
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

                List<String> ids = new ArrayList<>();
                for (BaseEntity entity : items) {
                    ids.add(entity.getId());
                    Log.d(TAG, "run: IDS : " + entity.getId());
                }


                try {
                    Response response = getClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.d(TAG, String.format("%s uploaded", path));
                        listener.onMetricUpload(ids, filePaths);
                    } else if (!response.isSuccessful() && response.code() == 500) {
                        // String successResponse = new Gson().toJson(response.body());
                        Log.d(TAG, "run: Updating sync status to 1");
                        listener.onMetricUpload(ids, filePaths);
                        Log.d(TAG, "run: Response Code : " + response.body().string() + ": Response message : " + response.message());
                        Log.d(TAG, String.format("%s upload failed with error : %s", path, response.message()));
                    }
                } catch (IOException e) {
                    Log.e(TAG, String.format("Failed to upload %s", path), e);
                }
            }
        });
    }


    private void checkConfigStats() {

        Log.d(TAG, "checkConfigStats: Entereds");

        Request request =  new Request.Builder()
                .url(String.format("%s/%s/%s/%s", BASE_URL, "config", "projectbyApiKey", KEY))
                .get()
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();

        //{"startup":true,"gc":true,"network":true,"method":true,"API":true,"FPS":true,"memoryUsage":true,"memoryLeak":true,"screenTransition":true}
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Success");
                JSONObject responseObject = new JSONObject(response.body().string());

                //  Log.d(TAG, "checkConfigStats: Response String : " + response.body().string());
                JSONObject configJSONObject = new JSONObject(responseObject.get("config").toString());

                String fps = configJSONObject.get("FPS").toString();
                String gcs = configJSONObject.get("gc").toString();
                String memory_leak = configJSONObject.get("memoryLeak").toString();
// FPS, GC, Memory Leak, Network, Memory Usage, Battery
                String network_usage = configJSONObject.get("network").toString(); //Add these key values in the JSON response from the backend.
                String memory_usage = configJSONObject.get("memoryUsage").toString(); //Add these key values in the JSON response from the backend.
                String battery_stats = configJSONObject.get("battery").toString(); //Add these key values in the JSON response from the backend.
// Change to "battery"
                Log.d(TAG, "Values: Called + " + fps + " + " + gcs + " : " + memory_leak + " : " + network_usage + " : " + memory_usage + " : " + battery_stats);

                //Log.d(TAG, "checkConfigStats: " + gcsStatus);

                //configUploaded();

                // saveMetricDetails(fps, gcs, memory_leak, network_usage, memory_usage, battery_stats);
                triggerMetrics(fps, gcs, memory_leak, network_usage, memory_usage, battery_stats);

            }
            else {
                Log.d(TAG, "checkConfigStats: ERROR : " + response.message());
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void triggerMetrics(String fps, String gcs, String memory_leak, String network_usage, String memory_usage, String battery_stats) {

        //Update the values to the shared pref


        Log.d(TAG, "SyncManager triggerMetrics: Called : FPS = " + fps + " + GC : " + gcs + " : Memory Leak :  "
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

    private void saveMetricDetails(String fps, String gcs, String memory_leak, String network_usage,
                                   String memory_usage, String battery_stats) {


        appachhiPrefEditor = appachhiPref.edit();

        appachhiPrefEditor.putString("fps_status", fps);
        appachhiPrefEditor.putString("gcs_status", gcs);
        appachhiPrefEditor.putString("memory_leak_status", memory_leak);
        appachhiPrefEditor.putString("network_usage_status", network_usage);
        appachhiPrefEditor.putString("memory_usage_status", memory_usage);
        appachhiPrefEditor.putString("battery_stats_status", memory_usage);

        appachhiPrefEditor.apply();


        //Appachhi.getInstance().checkConfigStats();
    }

    private Request getRequest(String path, String contentArray) {
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), contentArray);

        // Create a new endpoint to get the project details - change path variable.
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

    public boolean isConfigFetched() {
        return appachhiPref.getBoolean(CONFIG_FETCHED, false);
    }

    public void configUploaded() {
        appachhiPref.edit().putBoolean(CONFIG_FETCHED, true).apply();
        Log.d(TAG, "configUploaded: flag set to true");

    }

    private boolean isDeviceDetailUploaded() {
        return appachhiPref.getBoolean(DEVICE_ID_UPLOADED, false);
    }

    private void deviceIDUploaded() {
        appachhiPref.edit().putBoolean(DEVICE_ID_UPLOADED, true).apply();
        Log.d(TAG, "deviceIDUploaded: flag set to true");
    }

  /*  @SuppressLint("HardwareIds")
    private String getDeviceId() {
        String storedDeviceId = appachhiPref.getString(DEVICE_ID_KEY, null);
        if (storedDeviceId == null) {
            //storedDeviceId = appachhiPref.getString(DEVICE_ID_KEY, "Null");
            storedDeviceId = UUID.randomUUID().toString();
            appachhiPref.edit().putString(DEVICE_ID_KEY, storedDeviceId).apply();
        }
        Log.d(TAG, "getDeviceId: key : " + storedDeviceId);
        return storedDeviceId;
    }*/

    private String getDeviceId() {
        String storedDeviceId = appachhiPref.getString(DEVICE_ID_KEY, null);
        if (storedDeviceId == null) {
            storedDeviceId = UUID.randomUUID().toString();
            appachhiPref.edit().putString(DEVICE_ID_KEY, storedDeviceId).apply();
        }
        // Log.d(TAG, "getDeviceId: key : " + storedDeviceId);
        return storedDeviceId;
    }

    @SuppressLint("HardwareIds")
    private String getSecureId() {
        String storedSecureId = appachhiPref.getString(SECURE_ID_KEY, null);
        if (storedSecureId == null) {
            storedSecureId = appachhiPref.getString(SECURE_ID_KEY, "Null");
            //storedDeviceId = UUID.randomUUID().toString();
            appachhiPref.edit().putString(SECURE_ID_KEY, storedSecureId).apply();
        }
        //Log.d(TAG, "getSecureDeviceId: key : " + storedSecureId);
        return storedSecureId;
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
            jsonObject.put("secureId", getSecureId());
            jsonObject.put("manufacturer", Build.BRAND);
            jsonObject.put("model", Build.MODEL);
            jsonObject.put("os", "android");
            jsonObject.put("osVersion", Build.VERSION.SDK_INT);

            jsonObject.put("lcddensity", deviceDataObject.getlcddensity());
            jsonObject.put("screenheight", deviceDataObject.getScreenheight());
            jsonObject.put("screenwidth", deviceDataObject.getscreenwidth());
            jsonObject.put("cpuarchitecture", deviceDataObject.getCPUarchitecture());

            jsonArray.put(jsonObject);

            Request request = getRequest("device", jsonArray.toString());
            Log.d(TAG, String.format("Url is %s", request.url().toString()));
            Response response = getClient().newCall(request).execute();
            Log.d(TAG, "uploadDeviceDetails: Response Code : " + response.code());
            if (response.isSuccessful() || response.code()==207 ) {

                //Log.d(TAG, "uploadDeviceDetails: response status : " + response.isSuccessful());
                deviceIDUploaded();

                //setDeviceID(response);
               /* ResponseBody r = response.body();
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> myMap = gson.fromJson(response.body().string() , type);
                String deviceIDfromResponse = myMap.get("id");*/

                JSONObject jsonObject2 = new JSONObject(response.body().string());
                String deviceIDfromResponse = jsonObject2.get("id").toString();

                // Log.d(TAG, "setDeviceID: ID : " + deviceIDfromResponse);

                //Log.d(TAG, "uploadDeviceDetails: Device ID from response : " + deviceIDfromResponse);

                //Log.d(TAG, "uploadDeviceDetails: Upload device details : " + deviceIDfromResponse);
                appachhiPref.edit().putString(DEVICE_ID_KEY, deviceIDfromResponse).apply();
                //Check response code

            } else {
                Log.d(TAG, String.format("Failed to upload device details : %s", response.message()));
                Log.d(TAG, "Response code : " + response.code());
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "Failed to upload device details" + e.getMessage());
        }

    }



    interface OnMetricUploadListener {
        void onMetricUpload(List<String> ids, List<String> filepaths);
    }
}