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


public class RetrofitInteceptor implements Interceptor {
    public static final String TAG = "RetrofitInterceptor";


    Request req;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.d(TAG, "intercept: Enterred");
        HttpMetric httpMetric = Appachhi.newHttpTrace();
        long startTime = httpMetric.start();


        Response res = chain.proceed(chain.call().request());
        long endTime = httpMetric.stop();

        if(res!=null) {

            Request request = res.request();
            if (request!=null) {
                httpMetric.setUrl(request.url().url().toString());
                httpMetric.setMethodType(request.method());
                long requestContentLength;

                if (request.body() != null && (requestContentLength = request.body().contentLength()) != -1L) {
                    httpMetric.setRequestContentLength(requestContentLength);
                }

                ResponseBody responseBody = res.body();
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

                httpMetric.setResponseCode(res.code());
                httpMetric.setStartTime(startTime);
                httpMetric.setEndTime(endTime);
                httpMetric.setThreadName(Thread.currentThread().getName());
                httpMetric.complete();

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                req = requestBuilder.build();
                return res;
            }

        }



        return chain.proceed(req);

    }
}
