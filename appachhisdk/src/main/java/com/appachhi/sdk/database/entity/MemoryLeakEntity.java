package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "memory_leak", foreignKeys = @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "session_id"))
public class MemoryLeakEntity extends BaseEntity {
    @ColumnInfo(name = "class_name")
    private String className;
    @ColumnInfo(name = "leak_trace")
    private String leakTrace;

    public MemoryLeakEntity(String className, String leakTrace, String sessionId) {
        super(sessionId);
        this.className = className;
        this.leakTrace = leakTrace;
    }


    @SuppressWarnings("WeakerAccess")
    public MemoryLeakEntity() {

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLeakTrace() {
        return leakTrace;
    }

    public void setLeakTrace(String leakTrace) {
        this.leakTrace = leakTrace;
    }
}