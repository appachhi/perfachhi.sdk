package com.appachhi.sdk.monitor.memory;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent memory information for a particular process as well as for the system
 */
@SuppressWarnings("WeakerAccess")
public class MemoryInfo {
    private static final String TAG = "MemoryInfo";
    private static final String SUMMARY_JAVA_HEAP = "summary.java-heap";
    private static final String SUMMARY_NATIVE_HEAP = "summary.native-heap";
    private static final String SUMMARY_CODE = "summary.code";
    private static final String SUMMARY_STACK = "summary.stack";
    private static final String SUMMARY_GRAPHICS = "summary.graphics";
    private static final String SUMMARY_PVT_OTHER = "summary.private-other";
    private static final String SUMMARY_SYSTEM = "summary.system";
    private static final String SUMMARY_TOTAL_SWAP = "summary.total-swap";
    private static final String THRESHOLD = "Threshold";
    private static final String TOTAL_PSS = "TotalPss";
    private static final String TOTAL_PRIVATE_DIRTY = "TotalPrivateDirty";
    private static final String TOTAL_SHARED_DIRTY = "TotalSharedDirty";
    private static final String JAVA_HEAP = "JavaHeap";
    private static final String TOTAL_NATIVE_HEAP_ = "TotalNative Heap ";
    private static final String CODE = "Code";
    private static final String STACK = "Stack";
    private static final String GRAPHICS = "Graphics";
    private static final String OTHER = "Other";
    private static final String SYSTEM_RESOURCE_MEMORY = "SystemResourceMemory";
    private static final String SWAP_MEMORY = "SwapMemory";
    private Debug.MemoryInfo memoryInfo;
    private ActivityManager.MemoryInfo systemMemoryInfo;

    MemoryInfo(Debug.MemoryInfo memoryInfo, ActivityManager.MemoryInfo systemMemoryInfo) {
        this.memoryInfo = memoryInfo;
        this.systemMemoryInfo = systemMemoryInfo;
    }

    /**
     * Memory from objects allocated from Java or Kotlin code.
     *
     * @return Memory occupied in KB
     */
    public int getJavaMemoryHeap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_JAVA_HEAP));
        }
        return memoryInfo.dalvikPrivateDirty;
    }

    /**
     * Memory from objects allocated from C or C++ code.
     *
     * <p>
     * Even if you're not using C++ in your app, you might see some native memory used here
     * because the Android framework uses native memory to handle various tasks on your behalf,
     * such as when handling image assets and other graphics—even though the code you've
     * written is in Java or Kotlin.
     * </p>
     *
     * @return Memory occupied in KB
     */
    public int getNativeMemoryHeap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_NATIVE_HEAP));
        }
        return memoryInfo.nativePrivateDirty;
    }

    /**
     * Memory that your app uses for code and resources, such as dex bytecode, optimized or
     * compiled dex code, .so libraries, and fonts.
     *
     * @return Memory occupied in KB
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getCodeMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_CODE));
    }

    /**
     * Memory used by both native and Java stacks in your app. This usually relates to how
     * many threads your app is running.
     *
     * @return Memory occupied in KB
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getStackMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_STACK));
    }

    /**
     * Memory used for graphics buffer queues to display pixels to the screen,
     * including GL surfaces, GL textures, and so on.
     * (Note that this is memory shared with the CPU, not dedicated GPU memory.)
     *
     * @return Memory occupied in KB
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getGraphicsMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_GRAPHICS));
    }

    /**
     * Memory used by your app that the system isn't sure how to categorize.
     *
     * @return Memory occupied in KB
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getOtherMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_PVT_OTHER));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getSystemResourceMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_SYSTEM));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getSwapMemory() {
        return Integer.parseInt(memoryInfo.getMemoryStat(SUMMARY_TOTAL_SWAP));
    }

    /**
     * Total Memory Allocated the app
     *
     * @return Memory occupied in KB
     */
    public int getTotalPssMemory() {
        return memoryInfo.getTotalPss();
    }

    /**
     * Total Private Memory Allocate to the app
     *
     * @return Memory occupied in KB
     */
    public int getTotalPrivateDirty() {
        return memoryInfo.getTotalPrivateDirty();
    }

    /**
     * Total Shared Memory allocated to the app
     *
     * @return Memory occupied in KB
     */
    public int getTotalSharedDirty() {
        return memoryInfo.getTotalSharedDirty();
    }

    /**
     * Max memory that can be allocated the the requested process
     *
     * @return Memory occupied in KB
     */
    public long getThreshold() {
        return systemMemoryInfo.threshold / 1024;
    }

    String asJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(THRESHOLD, getThreshold());
            jsonObject.put(TOTAL_PSS, getTotalPssMemory());
            jsonObject.put(TOTAL_PRIVATE_DIRTY, getTotalPrivateDirty());
            jsonObject.put(TOTAL_SHARED_DIRTY, getTotalSharedDirty());
            jsonObject.put(JAVA_HEAP, getJavaMemoryHeap());
            jsonObject.put(TOTAL_NATIVE_HEAP_, getNativeMemoryHeap());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                jsonObject.put(CODE, getCodeMemory());
                jsonObject.put(STACK, getStackMemory());
                jsonObject.put(GRAPHICS, getGraphicsMemory());
                jsonObject.put(OTHER, getOtherMemory());
                jsonObject.put(SYSTEM_RESOURCE_MEMORY, getSystemResourceMemory());
                jsonObject.put(SWAP_MEMORY, getSwapMemory());
            }
            return jsonObject.toString(2);
        } catch (JSONException e) {
            Log.d(TAG, "Failed to created JSON String");
            return "";
        }
    }
}