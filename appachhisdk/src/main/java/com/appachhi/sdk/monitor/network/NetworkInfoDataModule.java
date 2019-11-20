package com.appachhi.sdk.monitor.network;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import androidx.annotation.Nullable;

import com.appachhi.sdk.BaseDataModule;

/**
 * Data Module providing information about the data usage for the current process
 */
class NetworkInfoDataModule extends BaseDataModule<NetworkInfo> implements Runnable {
    /**
     * Network usage information for the current process when the tracking started.
     * It is kept here to provides the delta between the network once the tracing started and not
     * since on devices restart
     */
    private NetworkInfo initialNetworkInfo = new NetworkInfo(TrafficStats.getUidTxBytes(Process.myUid()),
            TrafficStats.getUidRxBytes(Process.myUid()));
    //Main thread handler
    private Handler handler = new Handler(Looper.getMainLooper());

    // Current Netwok information
    @Nullable
    private NetworkInfo data;
    // Polling interval for the network information
    private int interval;

    NetworkInfoDataModule(int interval) {
        this.interval = interval;
    }

    @Nullable
    @Override
    protected NetworkInfo getData() {
        return data;
    }

    @Override
    public void start() {
        handler.post(this);
    }

    @Override
    public void stop() {
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        // Polling the network information and returning the delta
        data = new NetworkInfo(
                TrafficStats.getUidTxBytes(Process.myUid()),
                TrafficStats.getUidRxBytes(Process.myUid()))
                .subtract(initialNetworkInfo);
        notifyObservers();
        handler.postDelayed(this, interval);
    }
}
