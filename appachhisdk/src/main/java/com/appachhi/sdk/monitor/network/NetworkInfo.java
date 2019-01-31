package com.appachhi.sdk.monitor.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing network usage information
 */
class NetworkInfo {
    private static final String TAG = "NetworkInfo";
    private static final String KB_SENT = "Send";
    private static final String KB_RECEIVE = "Receive";
    /**
     * Amount of bytes sent
     */
    private long byteSend;
    /**
     * Amount of bytes received
     */
    private long byteReceived;

    NetworkInfo(long byteSend, long byteReceived) {
        this.byteSend = byteSend;
        this.byteReceived = byteReceived;
    }

    long getByteSend() {
        return byteSend;
    }

    long getByteReceived() {
        return byteReceived;
    }

    /**
     * Subtracts the current with the give network info and returns the delta
     *
     * @param otherNetworkInfo Give {@link NetworkInfo}
     * @return Delta between the two
     */
    NetworkInfo subtract(NetworkInfo otherNetworkInfo) {
        long deltaSend = this.byteSend - otherNetworkInfo.byteSend;
        long deltaReceived = this.byteReceived - otherNetworkInfo.byteReceived;
        return new NetworkInfo(deltaSend, deltaReceived);
    }

    String asJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KB_SENT, byteSend / 1024);
            jsonObject.put(KB_RECEIVE, byteReceived / 1024);
            return jsonObject.toString(2);
        } catch (JSONException e) {
            Log.d(TAG, "Failed to created JSON String");
            return "";
        }

    }
}
