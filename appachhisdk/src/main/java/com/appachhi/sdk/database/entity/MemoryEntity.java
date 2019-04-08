package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "memory_usage", foreignKeys = @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "session_id"))
public class MemoryEntity extends BaseEntity {
    @ColumnInfo(name = "summary_java_heap")
    private String javaHeap;
    @ColumnInfo(name = "native_heap")
    private String nativeHeap;
    @ColumnInfo(name = "summary_code")
    private String code;
    @ColumnInfo(name = "summary_stack")
    private String stack;
    @ColumnInfo(name = "summary_graphics")
    private String graphics;
    @ColumnInfo(name = "summary_private_other")
    private String privateOther;
    @ColumnInfo(name = "summary_system")
    private String system;
    @ColumnInfo(name = "summary_total_swap")
    private String totalSwap;
    @ColumnInfo(name = "threshold")
    private String threshold;
    @ColumnInfo(name = "total_pss")
    private String totalPSS;
    @ColumnInfo(name = "total_private_dirty")
    private String totalPrivateDirty;
    @ColumnInfo(name = "total_shared_dirty")
    private String totalSharedDirty;
    @ColumnInfo(name = "system_resource_memory")
    private String systemResourceMemory;
    @ColumnInfo(name = "swap_memory")
    private String swapMemory;

    public MemoryEntity() {
    }

    public MemoryEntity(String javaHeap, String nativeHeap, String code, String stack,
                        String graphics, String privateOther, String system, String totalSwap,
                        String threshold, String totalPSS, String totalPrivateDirty,
                        String totalSharedDirty, String systemResourceMemory, String swapMemory,
                        String sessionId) {
        super(sessionId);
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

    public String getJavaHeap() {
        return javaHeap;
    }

    public void setJavaHeap(String javaHeap) {
        this.javaHeap = javaHeap;
    }

    public String getNativeHeap() {
        return nativeHeap;
    }

    public void setNativeHeap(String nativeHeap) {
        this.nativeHeap = nativeHeap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getPrivateOther() {
        return privateOther;
    }

    public void setPrivateOther(String privateOther) {
        this.privateOther = privateOther;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTotalSwap() {
        return totalSwap;
    }

    public void setTotalSwap(String totalSwap) {
        this.totalSwap = totalSwap;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getTotalPSS() {
        return totalPSS;
    }

    public void setTotalPSS(String totalPSS) {
        this.totalPSS = totalPSS;
    }

    public String getTotalPrivateDirty() {
        return totalPrivateDirty;
    }

    public void setTotalPrivateDirty(String totalPrivateDirty) {
        this.totalPrivateDirty = totalPrivateDirty;
    }

    public String getTotalSharedDirty() {
        return totalSharedDirty;
    }

    public void setTotalSharedDirty(String totalSharedDirty) {
        this.totalSharedDirty = totalSharedDirty;
    }

    public String getSystemResourceMemory() {
        return systemResourceMemory;
    }

    public void setSystemResourceMemory(String systemResourceMemory) {
        this.systemResourceMemory = systemResourceMemory;
    }

    public String getSwapMemory() {
        return swapMemory;
    }

    public void setSwapMemory(String swapMemory) {
        this.swapMemory = swapMemory;
    }
}
