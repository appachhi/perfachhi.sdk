package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;
//
//@Entity(tableName = "api_call", foreignKeys = @ForeignKey(
//        entity = Session.class,
//        parentColumns = "id",
//        childColumns = "session_id",
//        onDelete = ForeignKey.CASCADE),
//        indices = {@Index(name = "api_call_session_index", value = "session_id")})
public class APICallEntity extends BaseEntity {
    @SerializedName("url")
    private String url;

    @SerializedName("methodType")
    private String methodType;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("requestContentLength")
    private long requestContentLength;

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("duration")
    private long duration;

    @SerializedName("threadName")
    private String threadName;

    public APICallEntity(String url, String methodType, String contentType,
                         long requestContentLength, int responseCode,
                         long duration, String threadName, String sessionId,long sessionTime) {
        super(sessionId,sessionTime);
        this.url = url;
        this.methodType = methodType;
        this.contentType = contentType;
        this.requestContentLength = requestContentLength;
        this.responseCode = responseCode;
        this.threadName = threadName;
        this.duration = duration;
    }

    public APICallEntity() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getRequestContentLength() {
        return requestContentLength;
    }

    public void setRequestContentLength(long requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
