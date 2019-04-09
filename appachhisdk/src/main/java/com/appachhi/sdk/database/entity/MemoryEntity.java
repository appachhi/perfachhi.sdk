package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "memory_usage",
        foreignKeys = @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "session_id"),
        indices = {@Index(name = "memory_usage_session_index", value = "session_id")})
public class MemoryEntity extends BaseEntity {
    @ColumnInfo(name = "summary_java_heap")
    private int javaHeap;
    @ColumnInfo(name = "native_heap")
    private int nativeHeap;
    @ColumnInfo(name = "summary_code")
    private int code;
    @ColumnInfo(name = "summary_stack")
    private int stack;
    @ColumnInfo(name = "summary_graphics")
    private int graphics;
    @ColumnInfo(name = "summary_private_other")
    private int privateOther;
    @ColumnInfo(name = "summary_system")
    private int system;
    @ColumnInfo(name = "summary_total_swap")
    private int totalSwap;
    @ColumnInfo(name = "threshold")
    private long threshold;
    @ColumnInfo(name = "total_pss")
    private int totalPSS;
    @ColumnInfo(name = "total_private_dirty")
    private int totalPrivateDirty;
    @ColumnInfo(name = "total_shared_dirty")
    private int totalSharedDirty;
    @ColumnInfo(name = "system_resource_memory")
    private int systemResourceMemory;
    @ColumnInfo(name = "swap_memory")
    private int swapMemory;

    public MemoryEntity() {
    }

    @Ignore
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
