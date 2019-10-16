package com.appachhi.sdk.database;

import android.net.Uri;
import android.os.Build;

import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.LogsEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.ScreenshotEntity;
import com.appachhi.sdk.database.entity.TransitionStatEntity;
import com.appachhi.sdk.instrument.network.internal.HttpMetric;
import com.appachhi.sdk.instrument.trace.MethodTrace;
import com.appachhi.sdk.instrument.transition.TransitionStat;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfo;
import com.appachhi.sdk.monitor.memory.GCInfo;
import com.appachhi.sdk.monitor.memory.MemoryInfo;
import com.appachhi.sdk.monitor.memoryleak.MemoryLeakInfo;
import com.appachhi.sdk.monitor.network.NetworkInfo;
import com.squareup.leakcanary.AnalysisResult;

import java.io.File;

public class DatabaseMapper {
    public static CpuUsageEntity fromCpuUsageInfoToCpuUsageEntity(CpuUsageInfo cpuUsageInfo, String sessionId, long sessionTime) {
        return new CpuUsageEntity(cpuUsageInfo.getMyPid(), cpuUsageInfo.getTotal(), sessionId, sessionTime);
    }

    public static MemoryEntity fromMemoryInfoToMemoryEntity(MemoryInfo memoryInfo, String sessionId, long sessionTimeElapsed) {
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
                sessionId,
                sessionTimeElapsed
        );
    }

    public static GCEntity fromGCInfoToGCEntity(GCInfo gcInfo, String sessionId, long sessionTimeElapsed) {
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
                sessionId,
                sessionTimeElapsed
        );
    }

    public static FpsEntity fromDoubleToFpsEntity(double fps, String sessionId, long sessionTimeElapsed) {
        return new FpsEntity(fps, sessionId, sessionTimeElapsed);
    }

    public static NetworkUsageEntity fromNetworkUsageInfoToNetworkUsageEntity(NetworkInfo networkInfo, String sessionId, long sessionTimeElapsed) {
        return new NetworkUsageEntity(networkInfo.getByteSend(), networkInfo.getByteReceived(), sessionId, sessionTimeElapsed);
    }

    public static TransitionStatEntity fromTransitionStatToTransitionStatEntity(TransitionStat transitionStat, String sessionId, long sessionTimeElapsed) {
        return new TransitionStatEntity(transitionStat.getScreenName(), transitionStat.transitionDuration(), sessionId, sessionTimeElapsed);
    }

    public static MemoryLeakEntity fromMemoryLeakInfoTOMemoryLeakEntity(MemoryLeakInfo memoryLeakInfo, String sessionId, long sessionTimeElapsed) {
        AnalysisResult analysisResult = memoryLeakInfo.getAnalysisResult();
        return new MemoryLeakEntity(analysisResult.className,
                analysisResult.leakFound ? analysisResult.leakTrace != null ? analysisResult.leakTrace.toString() : null : null,
                sessionId,
                sessionTimeElapsed);
    }

    public static MethodTraceEntity fromMethodTraceToMethodTraceEntity(MethodTrace methodTrace, String sessionId, long sessionTimeElapsed) {
        return new MethodTraceEntity(methodTrace.getTraceName(), methodTrace.getDuration(), sessionId, sessionTimeElapsed);
    }

    public static ScreenshotEntity fromScreensShotFilePath(String filePath, String mimeType, String sessionId, long sessionTimElapsed) {
        return new ScreenshotEntity(sessionId, sessionTimElapsed, Uri.fromFile(new File(filePath)).getLastPathSegment(), filePath, mimeType);
    }

    public static LogsEntity fromLogsInfo(File file, String sessionId, long sessionTimeElapsed) {
        return new LogsEntity(sessionId, sessionTimeElapsed, Uri.fromFile(file).getLastPathSegment(), file.getAbsolutePath());
    }

    public static APICallEntity fromInterHttpMetricToApiCallEntity(HttpMetric httpMetric, String sessionId, long sessionTimeElapsed) {
        return new APICallEntity(
                httpMetric.getUrl(),
                httpMetric.getMethodType(),
                httpMetric.getContentType(),
                httpMetric.getRequestContentLength(),
                httpMetric.getResponseCode(),
                httpMetric.getDuration(),
                httpMetric.getThreadName(),
                sessionId,
                sessionTimeElapsed);

    }

    private static boolean isAboveAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
