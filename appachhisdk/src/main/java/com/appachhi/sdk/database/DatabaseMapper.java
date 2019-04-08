package com.appachhi.sdk.database;

import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfo;

public class DatabaseMapper {
    public static CpuUsageEntity fromCpuUsageInfoToCpuUsageEntity(CpuUsageInfo cpuUsageInfo, String sessionId) {
        return new CpuUsageEntity(cpuUsageInfo.getMyPid(), cpuUsageInfo.getTotal(), sessionId);
    }
}
