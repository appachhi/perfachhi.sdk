package com.appachhi.sdk.database.entity;


import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "memory_leak",
//        foreignKeys = @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "session_id"),
//        indices = {@Index(name = "memory_leak_session_index", value = "session_id")})
public class MemoryLeakEntity extends BaseEntity {
    @SerializedName("className")
    private String className;

    @SerializedName("leakTrace")
    private String leakTrace;

    public MemoryLeakEntity(String className, String leakTrace, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
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

    @Override
    public String toString() {
        return "MemoryLeakEntity{" +
                "className='" + className + '\'' +
                ", leakTrace='" + leakTrace + '\'' +
                '}';
    }

}