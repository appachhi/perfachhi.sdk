package com.appachhi.sdk.monitor.memoryleak;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appachhi.sdk.DataObserver;

import java.util.List;

public class MemoryLeakDataObserver implements DataObserver<List<MemoryLeakInfo>> {
    private static final String TAG = "Appachhi-MemoryLeak";

    @Override
    public void onDataAvailable(@NonNull List<MemoryLeakInfo> data) {
        if (data.size() > 0) {
            Log.i(TAG, "Leaks :" + data.size());
        }
    }
}
