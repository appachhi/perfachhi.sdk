package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "cpu_usage", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "cpu_usage_session_index", value = "session_id")})
public class CpuUsageEntity extends BaseEntity {
    @ColumnInfo(name = "app_cpu_usage")
    private double appCpuUsage;
    @ColumnInfo(name = "device_cpu_usage")
    private double deviceCpuUsage;

    @Ignore
    public CpuUsageEntity(double appCpuUsage, double deviceCpuUsage, String sessionId) {
        super(sessionId);
        this.appCpuUsage = appCpuUsage;
        this.deviceCpuUsage = deviceCpuUsage;
    }

    public CpuUsageEntity() {
    }

    public double getAppCpuUsage() {
        return appCpuUsage;
    }

    public void setAppCpuUsage(double appCpuUsage) {
        this.appCpuUsage = appCpuUsage;
    }

    public double getDeviceCpuUsage() {
        return deviceCpuUsage;
    }

    public void setDeviceCpuUsage(double deviceCpuUsage) {
        this.deviceCpuUsage = deviceCpuUsage;
    }
}
