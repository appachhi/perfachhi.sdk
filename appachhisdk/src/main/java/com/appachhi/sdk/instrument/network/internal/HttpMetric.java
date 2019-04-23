package com.appachhi.sdk.instrument.network.internal;

import android.support.annotation.NonNull;

import java.util.Date;

public class HttpMetric {
    private static final String TAG = "HttpMetric";
    @NonNull
    private String url;
    @NonNull
    private String methodType;
    private String contentType;
    private long requestContentLength;
    private long responseContentLength;
    private boolean isComplete;
    private boolean isStarted;
    private int responseCode;
    private long startTime;
    private long endTime;
    private String threadName;
    private HttpMetricSavingManager httpMetricSavingManager;

    public HttpMetric(HttpMetricSavingManager httpMetricSavingManager) {
        this.httpMetricSavingManager = httpMetricSavingManager;
        isComplete = false;
        isStarted = false;
        url = "";
        methodType = "";
        contentType = "";
        requestContentLength = 0;
        responseContentLength = 0;
    }

    public synchronized long start() {
        if (isStarted)
            throw new IllegalStateException("Cannot start request which is already running");
        if (isComplete) {
            throw new IllegalStateException("Cannot start request which is already complete");
        }
        isStarted = true;
        return new Date().getTime();
    }

    public synchronized long stop() {
        if (isComplete) {
            throw new IllegalStateException("Cannot stop request which is already complete");
        }
        return new Date().getTime();
    }

    public long getDuration() {
        if (!isComplete)
            throw new IllegalStateException("Cannot request duration of request which is not complete");
        return endTime - startTime;
    }

    public void setContentType(@NonNull String contentType) {
        this.contentType = contentType;
    }

    public void setMethodType(@NonNull String methodType) {
        this.methodType = methodType;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setRequestContentLength(long requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public void setResponseContentLength(long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void complete() {
        isComplete = true;
        httpMetricSavingManager.save(this);
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getMethodType() {
        return methodType;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }

    public long getRequestContentLength() {
        return requestContentLength;
    }

    public long getResponseContentLength() {
        return responseContentLength;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getThreadName() {
        return threadName;
    }

    @Override
    public String toString() {
        return "HttpMetric{" + "url='" + url + '\'' +
                ", methodType='" + methodType + '\'' +
                ", contentType='" + contentType + '\'' +
                ", requestContentLength=" + requestContentLength +
                ", responseContentLength=" + responseContentLength +
                ", isComplete=" + isComplete +
                ", responseCode=" + responseCode +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", threadName='" + threadName + '\'' +
                '}';
    }
}
