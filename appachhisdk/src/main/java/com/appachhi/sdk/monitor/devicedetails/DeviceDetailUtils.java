package com.appachhi.sdk.monitor.devicedetails;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapplication.model.DeviceDataObject;


public class DeviceDetailUtils {

    /* This function retrieves all device parameters via Android framework calls.
     * These values are bundled and sent via an Intent to the activity which will
     * access these values and stores in DB */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public DeviceDataObject fetchDeviceData(final Context context) {
        DeviceDataObject deviceobj = new DeviceDataObject();
        String modelid = null;
        String manufacturer;
        String softwareversion;
        String deviceid;
        int releaseid;
        float lcddensity;
        int screenheight;
        int screenwidth;
        String cpuarchitecture;
        String Secure_ID = null;
        double raminfoTotalsize = 0;
        double raminfoRemainingsize = 0;
        String perfBTname="";
        boolean cellularEnabled = false;
        boolean wifiEnabled = false;
        boolean btEnabled = false;
        final double MB_VALUE =  1048576.0;

        /* Get BT status*/
        BluetoothAdapter perf_BTAdapter = BluetoothAdapter.getDefaultAdapter();
        assert perf_BTAdapter != null;
        if(perf_BTAdapter == null)
        {
            //Bluetooth not supported
            perfBTname = "NA";
        }
        else if(perf_BTAdapter.isEnabled()) {

            //Bluetooth is supported and enabled
            btEnabled = true;
            perfBTname = perf_BTAdapter.getName();
            assert perfBTname != null;

            if(perfBTname == null)

            {
                perfBTname = "NA";
            }
        }
        else{
            //Bluetooth is supported but disabled
            btEnabled = false;
                perfBTname = perf_BTAdapter.getName();
                assert perfBTname != null;
                if(perfBTname == null)
                {
                    perfBTname = "NA";
                }
        }

        /* Get Wi-Fi status. Not getting the transport status using hastransport */
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            WifiManager perf_WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            assert perf_WifiManager != null;
            try {
                if (perf_WifiManager.isWifiEnabled()) {
                    wifiEnabled = true;
                } else {
                    wifiEnabled = false;
                }
            }
            catch (Exception e) {
                Log.e("GenericException","Exception Occured");
            }
        }

        TelephonyManager perf_TelePhonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(perf_TelePhonyManager != null) {
            try {
                if (perf_TelePhonyManager.getSimState() == TelephonyManager.SIM_STATE_READY && perf_TelePhonyManager.isDataEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        cellularEnabled = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 1) == 1;

                    } else {
                        cellularEnabled = Settings.Secure.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
                    }
                }
            } catch (NoSuchMethodError e) {
                Log.e("NoSuchMethod", "Method not supported");
            }catch (Exception e){
                Log.e("Generic Exception","An Exception Occured");
            }
        }
        /* If value returned by the framework is NULL. Store it as NA so that the activity
         * that makes use of these values can store it that way in DB.*/
        try {
            if ((Build.MODEL) == null) {
                modelid = "NA";
                System.out.println("Device model returned NULL");
            } else {
                modelid = Build.MODEL;
            }
        }catch (NoSuchMethodError e){
            Log.e("NoSuchMethod","Method not found");
        }

        if (Build.MANUFACTURER == null) {
            manufacturer = "NA";
            System.out.println("Manufacturer returned NULL");
        } else {
            manufacturer = Build.MANUFACTURER;
        }

        if (Build.VERSION.RELEASE == null) {
            softwareversion = "NA";
        } else {
            softwareversion = Build.VERSION.RELEASE;
        }

        if (Build.DEVICE == null) {
            deviceid = "NA";
        } else {
            deviceid = Build.DEVICE;
        }

        if (Build.VERSION.SDK_INT > 0) {
            releaseid = Build.VERSION.SDK_INT;
        } else {
            releaseid = 0;
        }

        if (Build.CPU_ABI != null) {
            cpuarchitecture = Build.CPU_ABI;
        } else {
            cpuarchitecture = "NA";
        }

        if (context.getResources().getDisplayMetrics().density > 0) {
            lcddensity = context.getResources().getDisplayMetrics().density;
        } else {
            lcddensity = 0;
        }

        if (context.getResources().getDisplayMetrics().heightPixels > 0) {
            screenheight = context.getResources().getDisplayMetrics().heightPixels;
        } else {
            screenheight = 0;
        }
        if (context.getResources().getDisplayMetrics().widthPixels > 0) {
            screenwidth = context.getResources().getDisplayMetrics().widthPixels;
        } else {
            screenwidth = 0;
        }

        Secure_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        assert Secure_ID != null;

        if (Secure_ID == null) {
            Secure_ID = "NA";
        }

        ActivityManager perfActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert perfActivityManager != null;
        
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        assert memoryInfo != null;
        perfActivityManager.getMemoryInfo(memoryInfo);
        raminfoTotalsize = memoryInfo.totalMem / MB_VALUE;
        raminfoRemainingsize = memoryInfo.availMem / MB_VALUE;

        /* Invoke set method defined in deviceobject to set the devicedetails values */
        deviceobj.setmodelid(modelid);
        deviceobj.setmanufacturer(manufacturer);
        deviceobj.setlcddensity(lcddensity);
        deviceobj.setScreenheight(screenheight);
        deviceobj.setscreenwidth(screenwidth);
        deviceobj.setreleaseid(releaseid);
        deviceobj.setdeviceid(deviceid);
        deviceobj.setsdklevel(releaseid);
        deviceobj.setsoftwareversion(softwareversion);
        deviceobj.setCPUarchitecture(cpuarchitecture);
        deviceobj.setSecure_ID(Secure_ID);
        deviceobj.setBtname(perfBTname);
        deviceobj.setRaminfo_remainingsize(raminfoRemainingsize);
        deviceobj.setRaminfo_totalsize(raminfoTotalsize);
        deviceobj.setBtEnabled(btEnabled);
        deviceobj.setWifiEnabled(wifiEnabled);
        deviceobj.setCellularEnabled(cellularEnabled);

        /*Return the object after populating it. This object will be accessed in Mainactivity*/
        return deviceobj;
    }
}

