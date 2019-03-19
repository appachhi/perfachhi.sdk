package com.appachhi.sdk.instrument.network.internal.okhttp;

import com.appachhi.sdk.instrument.network.internal.InternalHttpMetric;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCallWrapper implements Callback {
    private Callback actualCallback;
    private long startTime;
    private InternalHttpMetric httpMetric;

    OkHttpCallWrapper(Callback callback, InternalHttpMetric httpMetric, long startTime) {
        this.actualCallback = callback;
        this.startTime = startTime;
        this.httpMetric = httpMetric;

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Request request = call.request();
        if (request != null) {
            HttpUrl httpUrl;
            if ((httpUrl = request.url()) != null) {
                httpMetric.setUrl(httpUrl.url().toString());
            }

            if (request.method() != null) {
                httpMetric.setMethodType(request.method());
            }
        }

        httpMetric.setStartTime(startTime);
        httpMetric.setEndTime(httpMetric.stop());
        httpMetric.complete();
        actualCallback.onFailure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        AppachhiOkHttp3Client.handleCallSuccess(response, httpMetric, startTime, httpMetric.stop());
        actualCallback.onResponse(call,response);
    }
}
