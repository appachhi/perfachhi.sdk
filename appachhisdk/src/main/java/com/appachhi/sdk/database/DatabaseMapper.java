package com.appachhi.sdk.database;

import android.os.Build;

import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfo;
import com.appachhi.sdk.monitor.memory.MemoryInfo;

public class DatabaseMapper {
    public static CpuUsageEntity fromCpuUsageInfoToCpuUsageEntity(CpuUsageInfo cpuUsageInfo, String sessionId) {
        return new CpuUsageEntity(cpuUsageInfo.getMyPid(), cpuUsageInfo.getTotal(), sessionId);
    }

    public static MemoryEntity fromMemoryInfoToMemoryEntity(MemoryInfo memoryInfo, String sessionId) {
        return new MemoryEntity(
                memoryInfo.getJavaMemoryHeap(),
                memoryInfo.getNativeMemoryHeap(),
                isAboveAndroidM() ? memoryInfo.getCodeMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getStackMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getGraphicsMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getOtherMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getSystemResourceMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getSwapMemory() : 0,
                memoryInfo.getThreshold(),
                memoryInfo.getTotalPssMemory(),
                memoryInfo.getTotalPrivateDirty(),
                memoryInfo.getTotalSharedDirty(),
                isAboveAndroidM() ? memoryInfo.getSystemResourceMemory() : 0,
                isAboveAndroidM() ? memoryInfo.getSwapMemory() : 0,
                sessionId
        );
    }

    private static boolean isAboveAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
