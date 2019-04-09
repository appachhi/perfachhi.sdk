package com.appachhi.sdk.instrument.network.internal.okhttp;

import android.support.annotation.Keep;
import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.instrument.network.internal.InternalHttpMetric;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AppachhiOkHttp3Client {
    public static final String TAG = "AppachhiOKHttp3Client";

    @Keep
    public static Response execute(Call call) throws IOException {
        InternalHttpMetric httpMetric = Appachhi.newHttpTrace();
        long startTime = httpMetric.start();

        try {
            Response response = call.execute();
            long endTime = httpMetric.stop();
            handleCallSuccess(response, httpMetric, startTime, endTime);
            return response;
        } catch (IOException exception) {
            Request request;
            if ((request = call.request()) != null) {
                HttpUrl url = request.url();
                if (url != null) {
                    httpMetric.setUrl(url.url().toString());
                }

                if (request.method() != null) {
                    httpMetric.setMethodType(request.method());
                }
                RequestBody requestBody = request.body();
                if (requestBody != null && requestBody.contentLength() != -1) {
                    httpMetric.setRequestContentLength(requestBody.contentLength());
                }
            }

            httpMetric.setStartTime(startTime);
            httpMetric.setEndTime(httpMetric.stop());
            httpMetric.complete();
            throw exception;
        }
    }

    @Keep
    public static void enqueue(Call call, Callback callback) {
        Log.d(TAG, "enqueue: Before");
        InternalHttpMetric httpMetric = Appachhi.newHttpTrace();
        Log.d(TAG, "enqueue: AFter Creation");
        long startTime = httpMetric.start();
        Log.d(TAG, "enqueue: Metric start");
        call.enqueue(new OkHttpCallWrapper(callback, httpMetric, startTime));
        Log.d(TAG, "enqueue: After");
    }

    static void handleCallSuccess(Response response, InternalHttpMetric httpMetric,
                                  long startTime, long endTime) throws IOException {
        Request request = response.request();
        if (request != null) {
            httpMetric.setUrl(request.url().url().toString());
            httpMetric.setMethodType(request.method());
            long requestContentLength;
            if (request.body() != null && (requestContentLength = request.body().contentLength()) != -1L) {
                httpMetric.setRequestContentLength(requestContentLength);
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                long responseContentLength = responseBody.contentLength();
                if (responseContentLength != -1L) {
                    httpMetric.setResponseContentLength(responseContentLength);
                }

                MediaType mediaType;
                if ((mediaType = responseBody.contentType()) != null) {
                    httpMetric.setContentType(mediaType.toString());
                }
            }

            httpMetric.setResponseCode(response.code());
            httpMetric.setStartTime(startTime);
            httpMetric.setEndTime(endTime);
            httpMetric.setThreadName(Thread.currentThread().getName());
            httpMetric.complete();
        }
    }
}

