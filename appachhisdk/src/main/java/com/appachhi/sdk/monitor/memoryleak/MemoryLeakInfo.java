package com.appachhi.sdk.monitor.memoryleak;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.HeapDump;

public class MemoryLeakInfo {
    private HeapDump heapDump;
    private AnalysisResult analysisResult;
    private String leakInfo;

    MemoryLeakInfo(HeapDump heapDump, AnalysisResult analysisResult, String leakInfo) {
        this.heapDump = heapDump;
        this.analysisResult = analysisResult;
        this.leakInfo = leakInfo;
    }

    public HeapDump getHeapDump() {
        return heapDump;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public String getLeakInfo() {
        return leakInfo;
    }
}
