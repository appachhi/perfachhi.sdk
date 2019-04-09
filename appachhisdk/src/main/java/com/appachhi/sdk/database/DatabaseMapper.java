package com.appachhi.sdk.database;

import android.os.Build;

import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.appachhi.sdk.instrument.trace.MethodTrace;
import com.appachhi.sdk.instrument.transition.TransitionStat;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfo;
import com.appachhi.sdk.monitor.memory.GCInfo;
import com.appachhi.sdk.monitor.memory.MemoryInfo;
import com.appachhi.sdk.monitor.memoryleak.MemoryLeakInfo;
import com.appachhi.sdk.monitor.network.NetworkInfo;
import com.squareup.leakcanary.AnalysisResult;

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

    public static GCEntity fromGCInfoToGCEntity(GCInfo gcInfo, String sessionId) {
        return new GCEntity(
                gcInfo.getGcReason(),
                gcInfo.getGcName(),
                gcInfo.getObjectFreed(),
                gcInfo.getObjectFreedSize(),
                gcInfo.getAllocSpaceObjectFreed(),
                gcInfo.getAllocSpaceObjectFreedSize(),
                gcInfo.getLargeObjectFreedPercentage(),
                gcInfo.getLargeObjectFreedSize(),
                gcInfo.getLargeObjectTotalSize(),
                gcInfo.getGcPauseTime(),
                gcInfo.getGcRunTime(),
                sessionId
        );
    }

    public static FpsEntity fromDoubleToFpsEntity(double fps, String sessionId) {
        return new FpsEntity(fps, sessionId);
    }

    public static NetworkUsageEntity fromNetworkUsageInfoToNetworkUsageEntity(NetworkInfo networkInfo, String sessionId) {
        return new NetworkUsageEntity(networkInfo.getByteSend(), networkInfo.getByteReceived(), sessionId);
    }

    public static TransitionStatEntity fromTransitionStatToTransitionStatEntity(TransitionStat transitionStat, String sessionId) {
        return new TransitionStatEntity(transitionStat.getScreenName(), transitionStat.transitionDuration(), sessionId);
    }

    public static MemoryLeakEntity fromMemoryLeakInfoTOMemoryLeakEntity(MemoryLeakInfo memoryLeakInfo, String sessionId) {
        AnalysisResult analysisResult = memoryLeakInfo.getAnalysisResult();
        return new MemoryLeakEntity(analysisResult.className,
                analysisResult.leakFound ? analysisResult.leakTrace != null ? analysisResult.leakTrace.toString() : null : null, sessionId);
    }

    public static MethodTraceEntity fromMethodTraceToMethodTraceEntity(MethodTrace methodTrace, String sessionId) {
        return new MethodTraceEntity(methodTrace.getTraceName(), methodTrace.getDuration(), sessionId);
    }

    private static boolean isAboveAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
