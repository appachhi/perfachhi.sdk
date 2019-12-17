package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "gc", foreignKeys = @ForeignKey(
//        entity = Session.class,
//        parentColumns = "id",
//        childColumns = "session_id",
//        onDelete = ForeignKey.CASCADE),
//        indices = {@Index(name = "gc_session_index", value = "session_id")})
public class GCEntity extends BaseEntity {
    private String gcReason;
    // Type of GC
    @SerializedName("gcName")
    private String gcName;

    // Number of object was freed from heap
    @SerializedName("objectFreed")
    private String objectFreed;

    // Size of the object freed from heap
    @SerializedName("objectFreedSize")
    private String objectFreedSize;

    // Number of object was freed from Alloc Space
    @SerializedName("allocSpaceObjectFreed")
    private String allocSpaceObjectFreed;

    // Size of the object freed from Alloc Space
    @SerializedName("allocSpaceObjectFreedSize")
    private String allocSpaceObjectFreedSize;

    // Percentage of Large Object Freed
    @SerializedName("largeObjectFreedPercentage")
    private String largeObjectFreedPercentage;

    // Number of large object was freed
    @SerializedName("largeObjectFreedSize")
    private String largeObjectFreedSize;

    // Total Number of large object
    @SerializedName("largeObjectTotalSize")
    private String largeObjectTotalSize;

    // Time for which GC caused the pause in the main thread
    @SerializedName("gcPauseTime")
    private String gcPauseTime;

    // Duration for GC runtime
    @SerializedName("gcRunTime")
    private String gcRunTime;

    public GCEntity() {
    }

    public GCEntity(String gcReason, String gcName, String objectFreed, String objectFreedSize,
                    String allocSpaceObjectFreed, String allocSpaceObjectFreedSize,
                    String largeObjectFreedPercentage, String largeObjectFreedSize,
                    String largeObjectTotalSize, String gcPauseTime, String gcRunTime,
                    String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.gcReason = gcReason;
        this.gcName = gcName;
        this.objectFreed = objectFreed;
        this.objectFreedSize = objectFreedSize;
        this.allocSpaceObjectFreed = allocSpaceObjectFreed;
        this.allocSpaceObjectFreedSize = allocSpaceObjectFreedSize;
        this.largeObjectFreedPercentage = largeObjectFreedPercentage;
        this.largeObjectFreedSize = largeObjectFreedSize;
        this.largeObjectTotalSize = largeObjectTotalSize;
        this.gcPauseTime = gcPauseTime;
        this.gcRunTime = gcRunTime;
    }

    public String getGcReason() {
        return gcReason;
    }

    public void setGcReason(String gcReason) {
        this.gcReason = gcReason;
    }

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public String getObjectFreed() {
        return objectFreed;
    }

    public void setObjectFreed(String objectFreed) {
        this.objectFreed = objectFreed;
    }

    public String getObjectFreedSize() {
        return objectFreedSize;
    }

    public void setObjectFreedSize(String objectFreedSize) {
        this.objectFreedSize = objectFreedSize;
    }

    public String getAllocSpaceObjectFreed() {
        return allocSpaceObjectFreed;
    }

    public void setAllocSpaceObjectFreed(String allocSpaceObjectFreed) {
        this.allocSpaceObjectFreed = allocSpaceObjectFreed;
    }

    public String getAllocSpaceObjectFreedSize() {
        return allocSpaceObjectFreedSize;
    }

    public void setAllocSpaceObjectFreedSize(String allocSpaceObjectFreedSize) {
        this.allocSpaceObjectFreedSize = allocSpaceObjectFreedSize;
    }

    public String getLargeObjectFreedPercentage() {
        return largeObjectFreedPercentage;
    }

    public void setLargeObjectFreedPercentage(String largeObjectFreedPercentage) {
        this.largeObjectFreedPercentage = largeObjectFreedPercentage;
    }

    public String getLargeObjectFreedSize() {
        return largeObjectFreedSize;
    }

    public void setLargeObjectFreedSize(String largeObjectFreedSize) {
        this.largeObjectFreedSize = largeObjectFreedSize;
    }

    public String getLargeObjectTotalSize() {
        return largeObjectTotalSize;
    }

    public void setLargeObjectTotalSize(String largeObjectTotalSize) {
        this.largeObjectTotalSize = largeObjectTotalSize;
    }

    public String getGcPauseTime() {
        return gcPauseTime;
    }

    public void setGcPauseTime(String gcPauseTime) {
        this.gcPauseTime = gcPauseTime;
    }

    public String getGcRunTime() {
        return gcRunTime;
    }

    public void setGcRunTime(String gcRunTime) {
        this.gcRunTime = gcRunTime;
    }
}
