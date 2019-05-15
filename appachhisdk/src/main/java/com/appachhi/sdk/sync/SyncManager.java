package com.appachhi.sdk.sync;

import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.database.AppachhiDB;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.dao.SessionDao;
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
    private static final String KEY = "7fcfe510cbc28723c30cd874a71e6b2e";
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

    }

    private void uploadScreenTransition(ScreenTransitionDao screenTransitionDao) {
        Log.d(TAG, "upload Screen Transition Stat");
        // Fetch oldest 2000 UnSynced session
        List<TransitionStatEntity> transitionStatEntities = screenTransitionDao.oldest200UnSyncedTransitionStat();
        Log.d(TAG, String.format("%d transition state fetched", transitionStatEntities.size()));
        if (transitionStatEntities.isEmpty()){
            Log.d(TAG,"No screen transition to upload");
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
            Log.e(TAG, "Failed to upload sessions", e);
        }
    }

    private void uploadSessions(SessionDao sessionDao) {
        Log.d(TAG, "Upload Sessions");
        // Fetch oldest 2000 UnSynced session
        List<Session> sessions = sessionDao.oldest200UnSyncedSessions();
        Log.d(TAG, String.format("%d session fetched", sessions.size()));
        if (sessions.isEmpty()){
            Log.d(TAG,"No session to upload");
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
