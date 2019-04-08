package com.appachhi.sdk.monitor.cpu;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent CPU usage information for a particular process as well as for the system
 */
public class CpuUsageInfo {
    private static final String TAG = "CpuUsageInfo";
    private static final String DEVICE_CPU_USAGE = "DeviceCpuUsage";
    private static final String APP_CPU_USAGE = "AppCPuUsage";
    // Total CPU Usage Percentage by the device
    private final double total;
    // Total CPU Usage Percentage by the app
    private final double myPid;

    CpuUsageInfo(double total, double myPid) {
        this.total = total;
        this.myPid = myPid;
    }

    /**
     * Return the total CPU usage by the device in term of percentage
     * @return CPU Usage Percent
     */
    public double getTotal() {
        return total;
    }

    /**
     * Return the total CPU usage by the app in term of percentage
     * @return CPU Usage Percent
     */
   public double getMyPid() {
        return myPid;
    }

    String asJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DEVICE_CPU_USAGE, getTotal());
            jsonObject.put(APP_CPU_USAGE, getMyPid());
            return jsonObject.toString(2);
        } catch (JSONException e) {
            Log.d(TAG, "Failed to created JSON String");
            return "";
        }
    }
}
