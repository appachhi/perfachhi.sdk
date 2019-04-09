package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "api_call", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "api_call_session_index", value = "session_id")})
public class APICallEntity extends BaseEntity {
    private String url;
    @ColumnInfo(name = "method_type")
    private String methodType;
    @ColumnInfo(name = "content_type")
    private String contentType;
    @ColumnInfo(name = "request_content_length")
    private long requestContentLength;
    @ColumnInfo(name = "response_code")
    private int responseCode;
    private long duration;
    @ColumnInfo(name = "thread_name")
    private String threadName;

    @Ignore
    public APICallEntity(String url, String methodType, String contentType,
                         long requestContentLength, int responseCode,
                         long duration, String threadName, String sessionId) {
        super(sessionId);
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
