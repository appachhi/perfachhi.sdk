package com.appachhi.sdk.monitor.cpu;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appachhi.sdk.BaseDataModule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A Data Module providing information about the CPU Usage for the device as well as the app
 * This module work for device below Android O.For devices with Android O and above,this modules
 * works as no-ops
 * <p>
 * {@see <a href="https://issuetracker.google.com/issues/37091475">Android Product Forum<>}
 *
 * Information are extracted from Proc file which are updated by the kernel for the device
 * as well as for the app
 *
 * You can find the detail for Proc File Data here
 * {@see <a href="https://www.kernel.org/doc/Documentation/filesystems/proc.txt">Proc File Information</a>}
 */
class CpuUsageInfoDataModule extends BaseDataModule<CpuUsageInfo> {

    private static final String TAG = "CpuUsageInfoDataModule";

    // Main Thread Handler for posting the data on to the main thread
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final AtomicReference<CpuUsageInfo> cpuUsage = new AtomicReference<>();

    // Thread used for reading information continuously
    private ReaderThread cpuReaderThread;

    private final int interval;

    CpuUsageInfoDataModule(int interval) {
        this.interval = interval;
    }

    @Override
    public void start() {

        // Doesnot work from Android O onwards
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.w(TAG, "CpuUsageInfoModule is not supported on Android O and above and will be no-op.");
            return;
        }
        if (cpuReaderThread == null) {
            // Starts a new thread already doesnot exist
            cpuReaderThread = new ReaderThread();
            cpuReaderThread.start();
        }
    }

    @Override
    public void stop() {
        // Stop notify the data to the observer
        handler.removeCallbacks(notifyObserversRunnable);
        if (cpuReaderThread != null) {
            // Interrupts the thread
            cpuReaderThread.cancel();
            try {
                // Joins the Main Thread with the reader thread
                cpuReaderThread.join();
            } catch (InterruptedException ignore) {
            }
            cpuReaderThread = null;
        }
    }

    private final Runnable notifyObserversRunnable = new Runnable() {
        @Override
        public void run() {
            notifyObserver();
        }
    };

    @Nullable
    @Override
    protected CpuUsageInfo getData() {
        return cpuUsage.get();
    }

    private class ReaderThread extends Thread {

        private BufferedReader totalCpuReader;

        private BufferedReader myPidCpuReader;

        /* Jiffy is a unit of CPU time. */
        // Total Jiffies since  started calculating for the device
        private long totalJiffies;

        // Total Jiffies since  started calculating before computing the usage for the device
        private long totalJiffiesBefore;

        // Total Jiffies computed for the device
        private long jiffies;

        // Total Jiffies since  started calculating before computing the usage for the app
        private long jiffiesBefore;

        // Total Jiffies computed for the app
        private long jiffiesMyPid;

        // Total Jiffies since  started calculating before computing the usage for the app
        private long jiffiesMyPidBefore;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                openCpuReaders();
                read();
                closeCpuReaders();
                try {
                    Thread.currentThread();
                    sleep(interval);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        // Interrupts the current thread
        private  void cancel() {
            interrupt();
        }

        // Opens reader for reading data from the proc file for system and app
        private void openCpuReaders() {
            if (totalCpuReader == null) {
                try {
                    totalCpuReader = new BufferedReader(new FileReader("/proc/stat"));
                } catch (FileNotFoundException e) {
                    Log.w(TAG, "Could not open '/proc/stat' - " + e.getMessage());
                }
            }
            if (myPidCpuReader == null) {
                try {
                    myPidCpuReader = new BufferedReader(new FileReader("/proc/" + Process.myPid() + "/stat"));
                } catch (FileNotFoundException e) {
                    Log.w(TAG, "Could not open '/proc/" + Process.myPid() + "/stat' - " + e.getMessage());
                }
            }
        }

        // Ref... Section 1.8 in https://www.kernel.org/doc/Documentation/filesystems/proc.txt and manpage of proc
        private void read() {
            if (totalCpuReader != null) {
                try {
                    String[] cpuData = totalCpuReader.readLine().split("[ ]+", 9);
                    //Log.d(TAG, "CPU total:" + Arrays.toString(cpuData));
                    // add user, nice, and system
                    jiffies = Long.parseLong(cpuData[1]) + Long.parseLong(cpuData[2]) + Long.parseLong(cpuData[3]);
                    // ignore 'iowait' value since it is not reliable
                    totalJiffies = jiffies + Long.parseLong(cpuData[4]) + Long.parseLong(cpuData[6]) + Long.parseLong(cpuData[7]);
                } catch (IOException ie) {
                    Log.w(TAG, "Failed reading total cpu data - " + ie.getMessage());
                }
            }

            if (myPidCpuReader != null) {
                try {
                    String[] cpuData = myPidCpuReader.readLine().split("[ ]+", 18);
                    //Log.d(TAG, "CPU for mypid:" + Arrays.toString(cpuData));
                    // add utime, stime, cutime, and cstime
                    jiffiesMyPid = Long.parseLong(cpuData[13]) + Long.parseLong(cpuData[14])
                            + Long.parseLong(cpuData[15]) + Long.parseLong(cpuData[16]);
                } catch (IOException ie) {
                    Log.w(TAG, "Failed reading my pid cpu data - " + ie.getMessage());
                }
            }

            if (totalJiffiesBefore > 0) {
                long totalDiff = totalJiffies - totalJiffiesBefore;
                long jiffiesDiff = jiffies - jiffiesBefore;
                long jiffiesMyPidDiff = jiffiesMyPid - jiffiesMyPidBefore;

                cpuUsage.set(new CpuUsageInfo(getPercentInRange(100f * jiffiesDiff / totalDiff),
                        getPercentInRange(100f * jiffiesMyPidDiff / totalDiff)));

                handler.post(notifyObserversRunnable);
            }

            totalJiffiesBefore = totalJiffies;
            jiffiesBefore = jiffies;
            jiffiesMyPidBefore = jiffiesMyPid;
        }

        private void closeCpuReaders() {
            try {
                if (totalCpuReader != null) {
                    totalCpuReader.close();
                    totalCpuReader = null;
                }
                if (myPidCpuReader != null) {
                    myPidCpuReader.close();
                    myPidCpuReader = null;
                }
            } catch (IOException ignore) {
            }
        }
    }

    private static double getPercentInRange(double percent) {
        if (percent > 100f) {
            return 100f;
        } else if (percent < 0f) {
            return 0f;
        }
        return percent;
    }
}
