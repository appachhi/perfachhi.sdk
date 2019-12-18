package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "memory_usage",
//        foreignKeys = @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "session_id"),
//        indices = {@Index(name = "memory_usage_session_index", value = "session_id")})
public class MemoryEntity extends BaseEntity {
    @SerializedName("javaHeap")
    private int javaHeap;

    @SerializedName("nativeHeap")
    private int nativeHeap;

    @SerializedName("code")
    private int code;

    @SerializedName("stack")
    private int stack;

    @SerializedName("graphics")
    private int graphics;

    @SerializedName("privateOther")
    private int privateOther;

    @SerializedName("system")
    private int system;

    @SerializedName("totalSwap")
    private int totalSwap;

    @SerializedName("threshold")
    private long threshold;

    @SerializedName("totalPSS")
    private int totalPSS;

    @SerializedName("totalPrivateDirty")
    private int totalPrivateDirty;

    @SerializedName("totalSharedDirty")
    private int totalSharedDirty;

    @SerializedName("systemResourceMemory")
    private int systemResourceMemory;

    @SerializedName("swapMemory")
    private int swapMemory;

    public MemoryEntity() {
    }

    public MemoryEntity(int javaHeap, int nativeHeap, int code, int stack,
                        int graphics, int privateOther, int system, int totalSwap,
                        long threshold, int totalPSS, int totalPrivateDirty,
                        int totalSharedDirty, int systemResourceMemory, int swapMemory,
                        String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.javaHeap = javaHeap;
        this.nativeHeap = nativeHeap;
        this.code = code;
        this.stack = stack;
        this.graphics = graphics;
        this.privateOther = privateOther;
        this.system = system;
        this.totalSwap = totalSwap;
        this.threshold = threshold;
        this.totalPSS = totalPSS;
        this.totalPrivateDirty = totalPrivateDirty;
        this.totalSharedDirty = totalSharedDirty;
        this.systemResourceMemory = systemResourceMemory;
        this.swapMemory = swapMemory;
    }

    public int getJavaHeap() {
        return javaHeap;
    }

    public void setJavaHeap(int javaHeap) {
        this.javaHeap = javaHeap;
    }

    public int getNativeHeap() {
        return nativeHeap;
    }

    public void setNativeHeap(int nativeHeap) {
        this.nativeHeap = nativeHeap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public int getGraphics() {
        return graphics;
    }

    public void setGraphics(int graphics) {
        this.graphics = graphics;
    }

    public int getPrivateOther() {
        return privateOther;
    }

    public void setPrivateOther(int privateOther) {
        this.privateOther = privateOther;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public int getTotalSwap() {
        return totalSwap;
    }

    public void setTotalSwap(int totalSwap) {
        this.totalSwap = totalSwap;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public int getTotalPSS() {
        return totalPSS;
    }

    public void setTotalPSS(int totalPSS) {
        this.totalPSS = totalPSS;
    }

    public int getTotalPrivateDirty() {
        return totalPrivateDirty;
    }

    public void setTotalPrivateDirty(int totalPrivateDirty) {
        this.totalPrivateDirty = totalPrivateDirty;
    }

    public int getTotalSharedDirty() {
        return totalSharedDirty;
    }

    public void setTotalSharedDirty(int totalSharedDirty) {
        this.totalSharedDirty = totalSharedDirty;
    }

    public int getSystemResourceMemory() {
        return systemResourceMemory;
    }

    public void setSystemResourceMemory(int systemResourceMemory) {
        this.systemResourceMemory = systemResourceMemory;
    }

    public int getSwapMemory() {
        return swapMemory;
    }

    public void setSwapMemory(int swapMemory) {
        this.swapMemory = swapMemory;
    }


}
