package com.appachhi.sdk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.appachhi.sdk.sync.SyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MetricConfigManager {
    private SharedPreferences appachhiPref;
    public static final String TAG = "MetricConfigManager";
    //private static final String BASE_URL = "https://perfachhi.appspot.com";
    private static final String BASE_URL = "https:/ff1c2ecaacbf.ngrok.io";

    private OkHttpClient okHttpClient;

    private static String KEY = null;


    public MetricConfigManager(Application application) {
        this.appachhiPref = application.getSharedPreferences("appachhi_pref", Context.MODE_PRIVATE);
        loadApiKey(application);

        getConfigDetails();
    }


    private static void loadApiKey(Application application) {
        Log.d(TAG, "loadApiKey: Entered");
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

    public void getConfigDetails() {
        Request request =  new Request.Builder()
                .url(String.format("%s/%s/%s/%s", BASE_URL, "project", "projectbyApiKey", KEY))
                .get()
                .addHeader("Authorization", String.format("Bearer %s", KEY))
                .build();
        try {
            Response response = getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Success");
                JSONObject responseObject = new JSONObject(response.body().string());

                JSONObject configJSONObject = new JSONObject(responseObject.get("config").toString());
                String fps = configJSONObject.get("fps").toString();
                String gcs = configJSONObject.get("gc").toString();
                String memory_leak = configJSONObject.get("memory_leak").toString();

                Log.d(TAG, "checkConfigStats: " + fps);

                saveMetricDetails(fps, gcs, memory_leak);
                // triggerMetrics(fps, gcs, memory_leak);


            }
            else {
                Log.d(TAG, "checkConfigStats: ERROR : " + response.message());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveMetricDetails(String fps, String gcs, String memory_leak) {

        if (fps.equals("true")) {
            Log.d(TAG, "saveMetricDetails: TRUE");

            Appachhi.getInstance().addFpsModule(true);
        } else {

            Log.d(TAG, "saveMetricDetails: TRUE");
            Appachhi.getInstance().addFpsModule(false);
        }

        SharedPreferences.Editor appachhiPrefEditor
                = appachhiPref.edit();

        appachhiPrefEditor.putString("fps_status", fps);
        appachhiPrefEditor.putString("gcs_status", gcs);
        appachhiPrefEditor.putString("memory_leak_status", memory_leak);
        appachhiPrefEditor.apply();



    }
}
