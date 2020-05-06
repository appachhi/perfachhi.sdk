package com.appachhi.sdk.instrument.network.internal.okhttp;

import android.util.Log;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.instrument.network.internal.HttpMetric;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RetrofitInterceptor implements Interceptor {
    public static final String TAG = "RetrofitInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpMetric httpMetric = Appachhi.newHttpTrace();
        long startTime = httpMetric.start();

        if (chain == null) {
            Log.d(TAG, "logtheCall: Response is null. Call was not executed");
        }
        else {
            try {
                long endTime = httpMetric.stop();
                handleCallSuccess(chain, httpMetric, startTime, endTime);

            } catch (IOException e) {
                Request request;
                if ((request = chain.request()) != null) {
                    HttpUrl url = request.url();
                    if (url!=null) {
                        httpMetric.setUrl(url.url().toString());
                    }
                    if (request.method() != null) {
                        httpMetric.setMethodType(request.method());
                    }

                    RequestBody requestBody = request.body();
                    if (requestBody !=null && requestBody.contentLength() != -1) {
                        httpMetric.setRequestContentLength(requestBody.contentLength());
                    }
                }

                httpMetric.setStartTime(startTime);
                httpMetric.setEndTime(httpMetric.stop());
                httpMetric.complete();

                e.printStackTrace();
            }
        }

        return null;
    }


    static void handleCallSuccess(Chain response, HttpMetric httpMetric,
                                  long startTime, long endTime) throws IOException {
        Request request = response.request();
        if (request != null) {
            httpMetric.setUrl(request.url().url().toString());
            httpMetric.setMethodType(request.method());
            long requestContentLength;
            if (request.body() != null && (requestContentLength = request.body().contentLength()) != -1L) {
                httpMetric.setRequestContentLength(requestContentLength);
            }

            RequestBody responseBody = response.request().body();
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

            httpMetric.setResponseCode(response.proceed(request).code());
            httpMetric.setStartTime(startTime);
            httpMetric.setEndTime(endTime);
            httpMetric.setThreadName(Thread.currentThread().getName());
            httpMetric.complete();
        }
    }
}