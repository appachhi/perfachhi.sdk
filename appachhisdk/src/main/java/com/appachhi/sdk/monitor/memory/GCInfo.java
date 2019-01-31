package com.appachhi.sdk.monitor.memory;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing the information collected for each gc run
 */
class GCInfo {
    private static final String TAG = "GCInfo";
    private static final String GC_REASON = "GCReason";
    private static final String GC_NAME = "GCName";
    private static final String OBJECT_FREED = "ObjectFreed";
    private static final String OBJECT_FREED_SIZE = "ObjectFreedSize";
    private static final String ALLOC_SPACE_OBJECT_FREED = "AllocSpaceObjectFreed";
    private static final String ALLOC_SPACE_OBJECT_FREED_SIZE = "AllocSpaceObjectFreedSize";
    private static final String LARGE_OBJECT_FREED_PERCENTAGE = "LargeObjectFreedPercentage";
    private static final String LARGE_OBJECT_FREED_SIZE = "LargeObjectFreedPercentage";
    private static final String LARGE_OBJECT_TOTAL_SIZE = "LargeObjectTotalSize";
    private static final String GC_PAUSE_TIME = "GCPauseTime";
    private static final String GC_RUNTIME = "GCRunTime";
    // Reason why GC ran
    private String gcReason;
    // Type of GC
    private String gcName;
    // Number of object was freed from heap
    private String objectFreed;
    // Size of the object freed from heap
    private String objectFreedSize;
    // Number of object was freed from Alloc Space
    private String allocSpaceObjectFreed;
    // Size of the object freed from Alloc Space
    private String allocSpaceObjectFreedSize;
    // Percentage of Large Object Freed
    private String largeObjectFreedPercentage;
    // Number of large object was freed
    private String largeObjectFreedSize;
    // Total Number of large object
    private String largeObjectTotalSize;
    // Time for which GC caused the pause in the main thread
    private String gcPauseTime;
    // Duration for GC runtime
    private String gcRunTime;

    GCInfo(String gcReason, String gcName, String objectFreed, String objectFreedSize,
           String allocSpaceObjectFreed, String allocSpaceObjectFreedSize,
           String largeObjectFreedPercentage, String largeObjectFreedSize,
           String largeObjectTotalSize, String gcPauseTime, String gcRunTime) {
        this.gcReason = gcReason;
        this.gcName = gcName;
        this.objectFreed = objectFreed;
        this.objectFreedSize = objectFreedSize;
        this.allocSpaceObjectFreed = allocSpaceObjectFreed;
        this.allocSpaceObjectFreedSize = allocSpaceObjectFreedSize;
        this.largeObjectFreedPercentage = largeObjectFreedPercentage;
        this.largeObjectFreedSize = largeObjectFreedSize;
        this.largeObjectTotalSize = largeObjectTotalSize;
        this.gcPauseTime = gcPauseTime;
        this.gcRunTime = gcRunTime;
    }

    String asJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GC_REASON, gcReason);
            jsonObject.put(GC_NAME, gcName);
            jsonObject.put(OBJECT_FREED, objectFreed);
            jsonObject.put(OBJECT_FREED_SIZE, objectFreedSize);
            jsonObject.put(ALLOC_SPACE_OBJECT_FREED, allocSpaceObjectFreed);
            jsonObject.put(ALLOC_SPACE_OBJECT_FREED_SIZE, allocSpaceObjectFreedSize);
            jsonObject.put(LARGE_OBJECT_FREED_PERCENTAGE, largeObjectFreedPercentage);
            jsonObject.put(LARGE_OBJECT_FREED_SIZE, largeObjectFreedSize);
            jsonObject.put(LARGE_OBJECT_TOTAL_SIZE, largeObjectTotalSize);
            jsonObject.put(GC_PAUSE_TIME, gcPauseTime);
            jsonObject.put(GC_RUNTIME, gcRunTime);
            return jsonObject.toString(2);
        } catch (JSONException e) {
            Log.d(TAG, "Failed to created JSON String");
            return "";
        }

    }
}
