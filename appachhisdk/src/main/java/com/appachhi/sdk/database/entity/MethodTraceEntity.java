package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;
//
//@Entity(tableName = "method_trace", foreignKeys = @ForeignKey(
//        entity = Session.class,
//        parentColumns = "id",
//        childColumns = "session_id",
//        onDelete = ForeignKey.CASCADE),
//        indices = {@Index(name = "method_trace_session_index", value = "session_id")})
public class MethodTraceEntity extends BaseEntity {
    @SerializedName("traceName")
    private String traceName;

    @SerializedName("duration")
    private long duration;

    public MethodTraceEntity(String traceName, long duration, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.traceName = traceName;
        this.duration = duration;
    }

    public MethodTraceEntity() {
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
