package com.appachhi.sdk.monitor.memoryleak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzedHeap;
import com.squareup.leakcanary.CanaryLog;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.squareup.leakcanary.LeakCanary.leakInfo;

@SuppressLint("PrivateResource")
public class AppachhiMemoryLeakService extends AbstractAnalysisResultService {

    public static final String NOTIFICATION_CHANNEL_ID = "com.appachhi.sdk.memoryleak";

    @Override
    protected final void onHeapAnalyzed(@NonNull AnalyzedHeap analyzedHeap) {
        HeapDump heapDump = analyzedHeap.heapDump;
        AnalysisResult result = analyzedHeap.result;

        String leakInfo = leakInfo(this, heapDump, result, true);

        heapDump = renameHeapdump(heapDump);
        boolean resultSaved = saveResult(heapDump, result);

        if (resultSaved) {
           afterDefaultHandling(heapDump,result,leakInfo);
        } else {
            onAnalysisResultFailure(getString(R.string.leak_canary_could_not_save_text));
        }

    }

    @Override
    protected final void onAnalysisResultFailure(String failureMessage) {
        // Removed Implementation for Appachhi SDK
    }

    private boolean saveResult(HeapDump heapDump, AnalysisResult result) {
        File resultFile = AnalyzedHeap.save(heapDump, result);
        return resultFile != null;
    }

    private HeapDump renameHeapdump(HeapDump heapDump) {
        String fileName =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS'.hprof'", Locale.US).format(new Date());

        File newFile = new File(heapDump.heapDumpFile.getParent(), fileName);
        boolean renamed = heapDump.heapDumpFile.renameTo(newFile);
        if (!renamed) {
            CanaryLog.d("Could not rename heap dump file %s to %s", heapDump.heapDumpFile.getPath(),
                    newFile.getPath());
        }
        return heapDump.buildUpon().heapDumpFile(newFile).build();
    }

    /**
     * You can override this method and do a blocking call to a server to upload the leak trace and
     * the heap dump. Don't forget to check {@link AnalysisResult#leakFound} and {@link
     * AnalysisResult#excludedLeak} first.
     */
    protected void afterDefaultHandling(@NonNull HeapDump heapDump, @NonNull AnalysisResult result,
                                        @NonNull String leakInfo) {
        Intent notifyLeakListener = new Intent(MemoryLeakDataModule.LEAK_BROADCAST_ACTION);
        notifyLeakListener.putExtra(MemoryLeakDataModule.LEAK_EXTRA_ANALYSIS_RESULT, result);
        notifyLeakListener.putExtra(MemoryLeakDataModule.LEAK_EXTRA_HEAP_DUMP, heapDump);
        notifyLeakListener.putExtra(MemoryLeakDataModule.LEAK_EXTRA_LEAK_INFO, leakInfo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notifyLeakListener);
    }
}
