package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "method_trace", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "method_trace_se[ssion_index", value = "session_id")})
public class MethodTraceEntity extends BaseEntity {
    @ColumnInfo(name = "name")
    private String traceName;
    private long duration;

    @Ignore
    public MethodTraceEntity(String traceName, long duration, String sessionId) {
        super(sessionId);
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
