package com.appachhi.sdk.monitor.battery;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaCodecInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

// This import, eventhough it is not used is kept in this file
// since there is a code that uses java reflection which is
// commented out for battery consumption calculation and might
// be of use later
import java.lang.reflect.InvocationTargetException;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BatteryBasicDetails {
    private long perfBatteryCapacity;
    private float perfBatteryVoltage,perfBatteryTemperature,perfBatteryChargingRate;
    private String perfBatteryTechnology,perfChargingStatusText,perfBatteryHealthLabel;
    private DataListener dataListener;
    private String perfPluggedInSource,perfLifeCycleState;
    private float perfBatteryPercentage,perfBatteryAvailablemA;
    private int perfNetworkTypeValue;
    String perfPowerSaveMode = "",perfNetworkType;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
    String perfDateTime;
    String cellularstatetext="OFF";
    String gpsEnabled ="OFF";
    boolean wifiEnabled = false,cellularEnabled = false,btEnabled = false;
    String perfWiFiState = null, perfBTState;
    int perfWiFiStatevalue,perBTValue,screenstatus,perfDevicerotation;
    String perfScreenStatusText="",perfDeviceOrientationText = "";
    StringBuilder perfTaskActivitiesString = new StringBuilder();
    StringBuilder perfRecentTaskString = new StringBuilder();
    StringBuilder perfAppProcessString = new StringBuilder();
    boolean bthastransport = false, wifihastransport = false;
    boolean cellularhastransport = false,gpshastransport = false;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void fetchbatterydetails(Context context) {
        // Fetch Battery details - Start
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("ServiceCast") BluetoothManager btmanager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        Context context1;
        BluetoothAdapter btadaptor = btmanager.getAdapter();
        //dataListener = context;
        IntentFilter batteryintent = new IntentFilter();
        batteryintent.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryintent.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryintent.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryintent.addAction(Intent.ACTION_BATTERY_OKAY);
        batteryintent.addAction(Intent.ACTION_BATTERY_LOW);
        batteryintent.addAction(Intent.ACTION_POWER_USAGE_SUMMARY);
        batteryintent.addAction(Intent.ACTION_SCREEN_ON);
        batteryintent.addAction(Intent.ACTION_SCREEN_OFF);
        batteryintent.addAction(Intent.ACTION_SHOW_APP_INFO);
        batteryintent.addAction(Intent.ACTION_USER_BACKGROUND);
        batteryintent.addAction(Intent.ACTION_USER_FOREGROUND);
        batteryintent.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        batteryintent.addAction(Intent.ACTION_ALL_APPS);
        batteryintent.addAction(Intent.ACTION_APP_ERROR);
        batteryintent.addAction(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);
        batteryintent.addAction(Intent.ACTION_CALL_BUTTON);
        batteryintent.addAction(Intent.ACTION_CAMERA_BUTTON);
        batteryintent.addAction(Intent.ACTION_DATE_CHANGED);
        batteryintent.addAction(Intent.ACTION_HEADSET_PLUG);
        batteryintent.addAction(Intent.ACTION_MY_PACKAGE_REPLACED);
        batteryintent.addAction(Intent.ACTION_TIME_CHANGED);
        batteryintent.addAction(Intent.ACTION_TIME_TICK);
        batteryintent.addAction(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);
        batteryintent.addAction(Intent.ACTION_BOOT_COMPLETED);
        batteryintent.addAction(Intent.ACTION_CALL);
        batteryintent.addAction(wifiManager.WIFI_STATE_CHANGED_ACTION);
        batteryintent.addAction(btadaptor.ACTION_REQUEST_ENABLE);
        batteryintent.addAction(btadaptor.ACTION_STATE_CHANGED);
        batteryintent.addAction(String.valueOf(TelephonyManager.DATA_ACTIVITY_INOUT));
        batteryintent.addAction(String.valueOf(TelephonyManager.DATA_CONNECTED));
        batteryintent.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);

        context.registerReceiver(batteryDataReciever, batteryintent);

    } // Fetch Battery details - End

    /* This function retrieves updated data of battery statistics in BatteryDataReceiver call
     *  based on the Action registered in the intent */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void updatebatterydata(Intent intent, Context context){  //Update Battery Data - Start
        /* Create an instance of Device details */
        DeviceDetailUtils deviceutils = new DeviceDetailUtils();
        DeviceDataObject deviceData = deviceutils.fetchDeviceData(context);

        boolean perf_Batterypresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (perf_Batterypresent) {

            perfBatteryHealthLabel = perfBatteryHealthLabelFunc(intent);
            perfBatteryPercentage = perfBatteryPercentageFunc(intent);
            perfDateTime = simpleDateFormat.format(cal.getTime());
            perfLifeCycleState = perfLifeCycleStateFunc();
            perfPluggedInSource = perfPluggedInSourceFunc(intent);
            perfDeviceOrientationText = perfDeviceOrientationTextFunc(deviceData,context);

            perfAppProcessString = perfAppProcess(context);
            perfTaskActivitiesString = perfTaskActivitiesFunc(context);
            perfRecentTaskString = perfRecentTaskFunc(context);

            perfPowerSaveMode = perfPowerSaveModeFunc(context);

            btEnabled = btEnabledFunc();
            perfBTState = perfBTStateFunc();
            wifiEnabled = wifiEnabledfunc(context);
            perfWiFiState = perfWiFiStateFunc(context);
            gpsEnabled = perfgpsEnabledFunc(context);
            cellularstatetext = cellularstatetextFunc(context);

            perfNetworkType = perfNetworkTypeFunc(context);

            /* Check Transport via connectivity interfaces - start*/
            try {
                NetworkCapabilities perfNWCapabilities = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ConnectivityManager perfConnManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                    NetworkInfo perfNWInfo = perfConnManager.getActiveNetworkInfo();
                    if (perfNWInfo != null) {
                        perfNWCapabilities = perfConnManager.getNetworkCapabilities(perfConnManager.getActiveNetwork());
                        if (perfNWCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                            bthastransport = true;
                        }
                        if (perfNWCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            wifihastransport = true;
                        }
                        if (perfNWCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            cellularhastransport = true;
                        }
                    }
                }else{
                    Toast.makeText(context,"Device runs a version older than Android M, plz upgrade your OS",Toast.LENGTH_LONG).show();
                }
            }catch (NoSuchMethodError e){
                Log.e("NoSuchMethodException","No Method Found Exception");
            }catch (Exception e){
                Log.e("GeneralException","Generic Exception Occured");
            }
            /* Check Transport via connectivity interfaces - End*/

            perfScreenStatusText = perfScreenStatusTextFunc(intent,context);
            perfChargingStatusText = perfChargingStatusTextFunc(intent);
            perfBatteryTechnology = perfBatteryTechnologyFunc(intent);
            perfBatteryTemperature = perfBatteryTemperatureFunc(intent);
            perfBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            perfBatteryCapacity = (long) getBatteryCapacity(context);

            perfBatteryChargingRate = perfBatteryChargingRateFunc(context,intent);
            perfBatteryAvailablemA = perfBatteryAvailablemAFunc(context,intent);



        } else //No battery detected....
        {
            //Do nothing, no battery detected .....
            //Toast.makeText(context, "No Battery detected", Toast.LENGTH_LONG).show();
        }

        fetchbatterybasicdata(context); /*Now you have all the data. Go and populate object
                                          with the available data and pass it to main activity
                                          via datalistener interface's onDataReceive function
                                       */
    } // Update battery data - End


    /* All functions invoked for retreiving battery statistics are defined below
    *  These functions are invoked inside update updatebatterydata              */


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public float getPrevbatteryvalue(Context context){
        BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        if(perf_batterymanager != null) {

            long perfBatteryAvailablemA = (perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)) / 1000;
        }
        return perfBatteryAvailablemA;
    }

    public long getPrevtime(){
        long prevtimeinmillisecs;
        Calendar cal = Calendar.getInstance();
        long today = cal.getTimeInMillis();
        prevtimeinmillisecs = today;
        return prevtimeinmillisecs;
    }



    public static float getmyconsumption(float batteryconsumption, BatteryDataObject batteryDataObject){

        float myconsumption = 0,mypercentage;
        /* Power profiles for different interfaces in various states are defined below
         *  in terms of percentage of total power */
        int WIFI_ON_BATTERY_PERCENTAGE = 15;
        int WIFI_ACTIVE_BATTERY_PERCENTAGE = 20;
        int BT_ON_BATTERY_PERCENTAGE = 2;
        int BT_ACTIVE_BATTERY_PERCENTAGE = 5;
        int GPS_ON_BATTERY_PERCENTAGE = 2;
        int CELLULAR_ACTIVE_BATTERY_PERCENTAGE = 15;
        int CELLULAR_ON_BATTERY_PERCENTAGE = 10;
        int SCREEN_ON_BATTERY_PERCENTAGE = 25;
        int MISC_BATTERY_PERCENTAGE = 25;
        int cumulativepercentage=0;

        if(batteryDataObject.isGpsEnabled() == "ON"){
            cumulativepercentage = cumulativepercentage + GPS_ON_BATTERY_PERCENTAGE;
        }
        if(batteryDataObject.isWifiEnabled() == true){
            if(batteryDataObject.Wifihastransport() == true){
                cumulativepercentage = cumulativepercentage + WIFI_ACTIVE_BATTERY_PERCENTAGE;
            }else{
                cumulativepercentage = cumulativepercentage + WIFI_ON_BATTERY_PERCENTAGE;
            }
        }
        if(batteryDataObject.getCellularstatetext() == "ON"){
            if(batteryDataObject.cellularhastransport() == true){
                cumulativepercentage = cumulativepercentage + CELLULAR_ACTIVE_BATTERY_PERCENTAGE;
            }else{
                cumulativepercentage = cumulativepercentage + CELLULAR_ON_BATTERY_PERCENTAGE;
            }
        }
        if(batteryDataObject.isBtEnabled() == true){
            if(batteryDataObject.bthastransport()){
                cumulativepercentage = cumulativepercentage + BT_ACTIVE_BATTERY_PERCENTAGE;
            }else{
                cumulativepercentage = cumulativepercentage + BT_ON_BATTERY_PERCENTAGE;
            }
        }

        if(batteryDataObject.getScreenstatustext() == "SCREEN-ON"){
            cumulativepercentage = cumulativepercentage + SCREEN_ON_BATTERY_PERCENTAGE;
        }
        cumulativepercentage = cumulativepercentage + MISC_BATTERY_PERCENTAGE;
        mypercentage = 100 - cumulativepercentage;
        myconsumption = (myconsumption + batteryconsumption)* mypercentage/100;
        return myconsumption;
    }


    /* Retreive available Battery value - Start */
    private float perfBatteryAvailablemAFunc(Context context,Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                if (perf_batterymanager != null) {
                    perfBatteryAvailablemA = (perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)) / 1000;
                }
            } catch (NullPointerException e) {
                Log.e("NullPointerException", "No Such method found");
            } catch (Exception e) {
                Log.e("General Exception", "Exception occured ");
            }
            return perfBatteryAvailablemA;
        }else{
            // You are here since the device is running a version older than Lollipop
            Toast.makeText(context,"You are running a version older than Lollipop, plz upgrade your OS",Toast.LENGTH_LONG).show();
            return 0;
        }

    } /* Retreive available Battery value - End */

    /* Retreive Battery percentage - Start */
    private float perfBatteryChargingRateFunc(Context context,Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                if (perf_batterymanager != null) {
                    long batterycurrentvoltage = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
                    perfBatteryChargingRate = (float) batterycurrentvoltage / 1000;

                }
            }catch (NullPointerException e){
                Log.e("NullPointerException", "No Such method found");
            }catch (Exception e){
                Log.e("General Exception", "Exception occured ");
            }
            return perfBatteryChargingRate;
        }else{
            // You are here since the device is running a version older than Lollipop
            Toast.makeText(context,"You are running a version older than Lollipop, plz upgrade your OS",Toast.LENGTH_LONG).show();
            return 0;
        }

    } /* Retreive Battery percentage - End */

    /* Fetch Recent Task info - Start */
    private StringBuilder perfRecentTaskFunc(Context context){
        try {
            ActivityManager perfActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RecentTaskInfo> perfRecentTaskList = new ArrayList<>(perfActivityManager.getRecentTasks(Integer.MAX_VALUE,
                    ActivityManager.RECENT_WITH_EXCLUDED | ActivityManager.RECENT_IGNORE_UNAVAILABLE));
            ListIterator<ActivityManager.RecentTaskInfo> perfRecentTaskListIterator = perfRecentTaskList.listIterator();
            while (perfRecentTaskListIterator.hasNext()) {
                ActivityManager.RecentTaskInfo perfRecentTaskInfo = perfRecentTaskListIterator.next();
                perfRecentTaskString.append(perfRecentTaskInfo);
            }
        }catch (NullPointerException e){
            Log.e("NullPointerException", "No Such method found");
        }catch (Exception e){
            Log.e("General Exception", "Exception occured ");
        }
        return perfRecentTaskString;
    } /* Fetch Recent Task info - End */

    /* Fetch Task list in an activity - Start */
    private StringBuilder perfTaskActivitiesFunc(Context context){

        final ActivityManager perfListofactivities = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert perfListofactivities != null;
        List perfRunningAppTasks = perfListofactivities.getAppTasks();
        assert  perfRunningAppTasks != null;

        if(perfRunningAppTasks != null){
            for(int i=0;i < perfRunningAppTasks.size();i++){
                perfTaskActivitiesString.append(perfRunningAppTasks.get(i).toString());
            }
        }
        return perfTaskActivitiesString;
    } /* Fetch Task list in an activity - End */

    /* Fetch Process list - Start*/
    private StringBuilder perfAppProcess(Context context){

        final ActivityManager perfListofactivities = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert perfListofactivities != null;
        final List<ActivityManager.RunningAppProcessInfo> perfRunningProcessinfo = perfListofactivities.getRunningAppProcesses();
        assert perfRunningProcessinfo != null;


        List perfRunningAppTasks = perfListofactivities.getAppTasks();
        assert  perfRunningAppTasks != null;

        if(perfRunningProcessinfo != null)
        {
            for(int i=0;i<perfRunningProcessinfo.size(); i++){

                perfAppProcessString.append(perfRunningProcessinfo.get(i).toString() + "\n");
                String perfProcessName = perfRunningProcessinfo.get(i).processName;
                perfAppProcessString.append(perfProcessName+ "\n");
                perfAppProcessString.append(perfRunningProcessinfo.get(i).pkgList.toString() +"\n");
            }
        }
        return perfAppProcessString;
    } /* Fetch Process list - End*/


    /* Check Batteryhealth  - Start */
    private String perfBatteryHealthLabelFunc(Intent intent){

        int batteryhealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        perfBatteryHealthLabel = "UNKNOWN";

        /* Based on the constants returned by batterymanager assign correct label
         *  Check Batteryhealth statistics - start */
        switch (batteryhealth) {

            case BatteryManager.BATTERY_HEALTH_COLD:
                perfBatteryHealthLabel = "COLD";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                perfBatteryHealthLabel = "DEAD";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                perfBatteryHealthLabel = "GOOD";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                perfBatteryHealthLabel = "OVER VOLTAGE";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                perfBatteryHealthLabel = "UNSPECIFIED FAILURE";
                break;
            default:
                perfBatteryHealthLabel = "UNKNOWN";
                break;
        }

        return perfBatteryHealthLabel;
    } /* Check Batteryhealth  - End */

    /* Fetch Battery percentage - Start */
    private float perfBatteryPercentageFunc(Intent intent){

        int batterylevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batteryscale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if ((batterylevel != -1) && (batteryscale != -1)) {
            perfBatteryPercentage = (float) ((batterylevel / (float) batteryscale) * 100f);
        }
        return perfBatteryPercentage;
    } /* Fetch Battery percentage - End */

    /* Check whether app is in background or foreground - Start */
    private String perfLifeCycleStateFunc(){

        if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState()== Lifecycle.State.CREATED)
        {
            perfLifeCycleState = "BACKGROUND";
        }
        else if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)){
            perfLifeCycleState = "FOREGROUND";
        }

        return perfLifeCycleState;
    } /* Check whether app is in background or foreground - End */


    /* Identify power source for device - Start */
    private String perfPluggedInSourceFunc(Intent intent){

        int pluggedin = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        perfPluggedInSource = "NOTPLUGGEDIN";

            /* Check the constants returned by BatteryManger and assign the appropriate label
             Plugged in source */
        switch (pluggedin) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                perfPluggedInSource = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                perfPluggedInSource = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                perfPluggedInSource = "WIRELESS";
                break;
            default:
                perfPluggedInSource = "NOT-PLUGGED-IN";
                break;
        }
        return perfPluggedInSource;
    } /* Identify power source for device - End */


    /* Find the device screen orientation - Start */
    private String perfDeviceOrientationTextFunc(DeviceDataObject deviceData,Context context){
        /* Get window service and create an instance of type WindowManager.
         *  Get device rotation from WindowManager instance */
        WindowManager perfWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert perfWindowManager != null;
        perfDevicerotation = perfWindowManager.getDefaultDisplay().getRotation();

        /* Determine the display orientation based on rotation, height and width of screen - Start */
        if (((perfDevicerotation == Surface.ROTATION_0 || perfDevicerotation == Surface.ROTATION_180) && deviceData.getScreenheight() > deviceData.getscreenwidth()) || ((perfDevicerotation == Surface.ROTATION_90 || perfDevicerotation == Surface.ROTATION_270) && deviceData.getscreenwidth() > deviceData.getScreenheight())) {
            switch (perfDevicerotation) {
                case Surface.ROTATION_0:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    perfDeviceOrientationText = "PORTRAIT";
                    break;
                case Surface.ROTATION_90:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    perfDeviceOrientationText = "LANDSCAPE";
                    break;
                case Surface.ROTATION_180:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    perfDeviceOrientationText = "PORTRAIT";
                    break;
                case Surface.ROTATION_270:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    perfDeviceOrientationText = "LANDSCAPE";
                    break;
                default:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    perfDeviceOrientationText = "PORTRAIT";
                    break;
            }
        }
        else // Default orientation of device is Landscape
        {
            switch (perfDevicerotation) {
                case Surface.ROTATION_0:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    perfDeviceOrientationText = "LANDSCAPE";
                    break;
                case Surface.ROTATION_90:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    perfDeviceOrientationText = "PORTRAIT";
                    break;
                case Surface.ROTATION_180:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    perfDeviceOrientationText = "LANDSCAPE";
                    break;
                case Surface.ROTATION_270:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    perfDeviceOrientationText = "PORTRAIT";
                    break;
                default:
                    //Perf_DeviceOrientationValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    perfDeviceOrientationText = "LANDSCAPE";
                    break;
            }
        } /* Determine the display orientation based on rotation, height and width of screen - End */

        return perfDeviceOrientationText;
    }  /* Find the device screen orientation - End */

    /* Check whether powersave mode is on or not - Start */
    private String perfPowerSaveModeFunc(Context context){

        try {
            PowerManager perfPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (perfPowerManager.isPowerSaveMode()) {
                perfPowerSaveMode = "ON";
            } else {
                perfPowerSaveMode = "OFF";
            }
        }catch(Exception e)
        {
            Log.e("General Exception", "Found an exception in isPowerSaveMode");
        }catch (NoSuchMethodError error){
            Log.e("NoSuchMethod","No such method supported");
        }

        return perfPowerSaveMode;
    } /* Check whether powersave mode is on or not - End */

    /* Check if Bluetooth is enabled or not - Start */
    private boolean btEnabledFunc(){

        BluetoothAdapter perf_BTAdapter = BluetoothAdapter.getDefaultAdapter();
        assert perf_BTAdapter != null;
        try {

            if (perf_BTAdapter == null) {
                //Bluetooth not supported
                btEnabled = false;
            } else if (perf_BTAdapter.isEnabled()) {
                //Bluetooth is supported and enabled
                btEnabled = true;
            } else {
                //Bluetooth is supported but disabled
                btEnabled = false;
            }
        } catch (NoSuchMethodError error) {
            Log.e("NoSuchmethodError", "No such method");
        } catch (Exception e) {
            Log.e("GenericException", "Exception Occured in BlueTooth enabled call");
        }

        return btEnabled;
    } /* Check if Bluetooth is enabled or not - End */


    /* Check state of Bluetooth - Start */
    private String perfBTStateFunc(){

        BluetoothAdapter perf_BTAdapter = BluetoothAdapter.getDefaultAdapter();

        perBTValue = perf_BTAdapter.getState();
        switch (perBTValue) {
            case BluetoothAdapter.STATE_ON:
                perfBTState = "ON";
                break;
            case BluetoothAdapter.STATE_OFF:
                perfBTState = "OFF";
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                perfBTState = "CONNECTED";
                break;
            case BluetoothAdapter.ERROR:
                perfBTState = "ERROR";
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                perfBTState = "DISCONNECTED";
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                perfBTState = "CONNECTING";
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                perfBTState = "DISCONNECTING";
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                perfBTState = "TURNING-ON";
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                perfBTState = "TURNING-OFF";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + perBTValue);
        }
        return perfBTState;
    }/* Check state of Bluetooth - End */


    /* Check whether WiFi is enabled or not - Start */
    private boolean wifiEnabledfunc(Context context){
        /* Get Wi-Fi enabled status. This doesn't retrieve the status of whether
         * transport is active on this interface or not */
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            WifiManager perf_WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            assert perf_WifiManager != null;

            try {
                if (perf_WifiManager.isWifiEnabled()) {
                    wifiEnabled = true;
                } else {
                    wifiEnabled = false;
                }
            } catch (NoSuchMethodError error) {
                Log.e("NoSuchmethodError", "No such method");
            } catch (Exception e) {
                Log.e("GenericException", "Exception Occured in WiFi enabled call");
            }
        }

        return wifiEnabled;
    } /* Check whether WiFi is enabled or not - End */


    /* Retreive WiFi State, if it is enabled - Start */
    private String perfWiFiStateFunc(Context context){
            WifiManager perf_WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            /* Get WiFi State if it is enabled - Start */
            perfWiFiStatevalue = perf_WifiManager.getWifiState();
            switch (perfWiFiStatevalue) {
                case WifiManager.WIFI_STATE_DISABLED:
                    perfWiFiState = "DISABLED";
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    perfWiFiState = "DISABLING";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    perfWiFiState = "ENABLED";
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    perfWiFiState = "ENABLING";
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    perfWiFiState = "UNKNOWN";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + perfWiFiStatevalue);
            }
            return perfWiFiState;
        } /* Retreive WiFi State, if it is enabled - End */


    /* Check whether GPS is enabled or not - Start */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private String perfgpsEnabledFunc(Context context){
        /* Location service enabled status is retrieved here */

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                LocationManager perfLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                assert perfLocationManager != null;
                if (perfLocationManager.isLocationEnabled()) {
                    gpsEnabled = "ON";
                } else {
                    gpsEnabled = "OFF";
                }
            }else{
                int perflocationmode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                        Settings.Secure.LOCATION_MODE_OFF);
                 if(perflocationmode != Settings.Secure.LOCATION_MODE_OFF){
                     gpsEnabled = "ON";
                 }else{
                     gpsEnabled = "OFF";
                 }
            }
        } catch (NoSuchMethodError error) {
            Log.e("NoSuchmethodError", "No such method");
        } catch (Exception e) {
            Log.e("GenericException", "Exception Occured in GPS enabled call");
        }
        return gpsEnabled;
    } /* Check whether GPS is enabled or not - End */

    /* Cellular service enabled status is retrieved here. This does not retrieve the status
     * whether transport is active on cellular interface or not */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String cellularstatetextFunc(Context context){
        try {
            TelephonyManager perfTelePhonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (perfTelePhonyManager != null) {

                if (perfTelePhonyManager.getSimState() == TelephonyManager.SIM_STATE_READY && perfTelePhonyManager.isDataEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                        cellularEnabled = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
                        cellularstatetext = "ON";

                    } else {
                        cellularEnabled = Settings.Secure.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
                        cellularstatetext = "ON";
                        Toast.makeText(context,"Device runs a version older than Android JB, Plz upgrade",Toast.LENGTH_LONG).show();
                    }
                } else {
                    cellularstatetext = "OFF";
                    cellularEnabled = false;

                }
            }
        }catch (NoSuchMethodError e){
             Log.e("NoSuchMethod", "Method not supported");
        }catch (NullPointerException e){
             Log.e("NullPointerException","Null Pointer Exception");
        }catch (Exception e){
             Log.e("Generic Exception", "An Exception Occured");
        }
        return cellularstatetext;
    }

    /* Get the type of Cellular Network - Start */
    private String perfNetworkTypeFunc(Context context){
        TelephonyManager perfTelePhonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        perfNetworkTypeValue = perfTelePhonyManager.getNetworkType();
        switch (perfNetworkTypeValue) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                perfNetworkType = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                perfNetworkType = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                perfNetworkType = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                perfNetworkType = "eHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                perfNetworkType = "EVDO rev. 0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                perfNetworkType = "EVDO rev. A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                perfNetworkType = "EVDO rev. B";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                perfNetworkType = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                perfNetworkType = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                perfNetworkType = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                perfNetworkType = "HSPA+";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                perfNetworkType = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                perfNetworkType = "iDen";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                perfNetworkType = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                perfNetworkType = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                perfNetworkType = "Unknown";
                break;
            case TelephonyManager.NETWORK_TYPE_GSM:
                perfNetworkType = "GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                perfNetworkType = "IWLAN";
                break;
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                perfNetworkType = "SCDMA";
                break;
            default:
                throw new IllegalStateException("Unexpected Value" + perfNetworkTypeValue);
                //break;
        }
        return perfNetworkType;
    } /* Get the type of Cellular Network - End */

    /* Get Screen display status via display service - Start */
    private String perfScreenStatusTextFunc(Intent intent, Context context) {

        DisplayManager perf_displayservice = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        assert perf_displayservice != null;

        for (Display display : perf_displayservice.getDisplays()) {
            switch (screenstatus = display.getState()) {
                case Display.STATE_ON:
                    //The display is ON
                    perfScreenStatusText = "SCREEN-ON";
                    break;
                case Display.STATE_OFF:
                    //The display is OFF
                    perfScreenStatusText = "SCREEN-OFF";
                    break;
                case Display.STATE_DOZE:
                    //The display is dozing in a low power state.It is still on but is optimized
                    // for showing system-provided content while the device is non-interactive
                    perfScreenStatusText = "SCREEN-DOZE";
                    break;
                case Display.STATE_DOZE_SUSPEND:
                    //The display is dozing in a suspended low power state.it is still on but the
                    // CPU is not updating it
                    perfScreenStatusText = "SCREEN-DOZE-SUSPEND";
                    break;
                case Display.STATE_ON_SUSPEND:
                    //The display is in a suspended full power state; it is still on but the CPU is
                    // not updating it.
                    perfScreenStatusText = "SCREEN-ON-SUSPEND";
                default:
            }
        }
        return perfScreenStatusText;
    } /* Get Screen display status via display service - End */

    /* Get charging status text - Start */
    private String perfChargingStatusTextFunc(Intent intent){
        int chargingstatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        perfChargingStatusText = "UNKNOWN";

        /* Charging status constants retreived from BatteryManager class and
         * assign appropriate label for status of charging*/
        switch (chargingstatus) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                perfChargingStatusText = "CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                perfChargingStatusText = "DISCHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                perfChargingStatusText = "FULL";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                perfChargingStatusText = "NOT CHARGING";
                break;
            default:
                perfChargingStatusText = "UNKNOWN";
                break;
        }

        return perfChargingStatusText;
    } /* Get charging status text - End */

    /* Get battery temperature - Start */
    private float perfBatteryTemperatureFunc(Intent intent){
        perfBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        if (perfBatteryTemperature > 0) {
            perfBatteryTemperature = (float) perfBatteryTemperature / 10f;
        }
        return perfBatteryTemperature;
    } /* Get battery temperature - End */

    /* Get battery technology - Start */
    private String perfBatteryTechnologyFunc(Intent intent){
        if (intent.getExtras() != null) {
            perfBatteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        }
        return perfBatteryTechnology;
    } /* Get battery technology - End */

    /* Get Battery Capacity - Start */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private double getBatteryCapacity(Context context) {
        BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
       /* if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && (perf_batterymanager != null)) {
                long bCapacity = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                long bchargecounter = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
                //long baverage = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);

                if (bCapacity != 0 && bchargecounter != 0) {
                    double batteryvalue = (double) ((float) bchargecounter / (float) bCapacity * 100f) / 1000;
                    return batteryvalue;
                }

        } */
        /*else */
         // This part of code is not exposed since this is a hidden API  which can break anytime in Android's roadmap
            Object perfPowerProfile;
            double batteryvalue=0;
            final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
            try{
                perfPowerProfile = Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(context);

                batteryvalue = (double) Class.forName(POWER_PROFILE_CLASS).getMethod("getBatteryCapacity").invoke(perfPowerProfile);

            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return batteryvalue;

    } /* Get Battery Capacity - End */


    /* Update the battery data upon receiving the broadcast */
    private BroadcastReceiver batteryDataReciever = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onReceive(Context context, Intent intent) {
            updatebatterydata(intent,context);
        }
    };

    //Unregister the Broadcast Receiver service
    protected void onDestroy (Context context,Intent intent) {
        //super.onDestroy();
        this.onDestroy(context, intent);
        context.unregisterReceiver(batteryDataReciever);
    }

    public void setCustomObjectListener(DataListener listener) {
        this.dataListener = listener;
    }



    /* Populate values of Battery object and place it in batterydataobject of
    *  onDataReceive in DataListener. Main activity will pick data from this interface */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public BatteryDataObject fetchbatterybasicdata(final Context context) {
        BatteryDataObject perfBasicBattObj = new BatteryDataObject();

        perfBasicBattObj.setPerfBatteryCapacity(perfBatteryCapacity);
        perfBasicBattObj.setPerfBatteryTemperature(perfBatteryTemperature);
        perfBasicBattObj.setPerfBatteryChargingRate(perfBatteryChargingRate);
        perfBasicBattObj.setPerfBatteryTechnology(perfBatteryTechnology);
        perfBasicBattObj.setPerfBatteryVoltage(perfBatteryVoltage);
        perfBasicBattObj.setPerfChargingStatusText(perfChargingStatusText);
        perfBasicBattObj.setPerfBatteryHealthLabel(perfBatteryHealthLabel);
        perfBasicBattObj.setPerfPluggedInSource(perfPluggedInSource);
        perfBasicBattObj.setPerfBatteryPercentage(perfBatteryPercentage);
        perfBasicBattObj.setPerfLifeCycleState(perfLifeCycleState);
        perfBasicBattObj.setPerfBatteryAvailablemA(perfBatteryAvailablemA);
        perfBasicBattObj.setPerfDateTime(perfDateTime);
        perfBasicBattObj.setPerfPowerSaveMode(perfPowerSaveMode);
        perfBasicBattObj.setCellularstatetext(cellularstatetext);
        perfBasicBattObj.setGpsEnabled(gpsEnabled);
        perfBasicBattObj.setPerfNetworkType(perfNetworkType);
        perfBasicBattObj.setBtEnabled(btEnabled);
        perfBasicBattObj.setWifiEnabled(wifiEnabled);
        perfBasicBattObj.setPerfBTState(perfBTState);
        perfBasicBattObj.setPerfWiFiState(perfWiFiState);
        perfBasicBattObj.setScreenstatustext(perfScreenStatusText);
        perfBasicBattObj.setPerfDeviceOrientationText(perfDeviceOrientationText);
        perfBasicBattObj.setRotation(perfDevicerotation);
        perfBasicBattObj.setPerftasklist(perfTaskActivitiesString);
        perfBasicBattObj.setPerfProcesslist(perfAppProcessString);
        perfBasicBattObj.setPerf_RecentTaskList(perfRecentTaskString);
        perfBasicBattObj.setBthastransport(bthastransport);
        perfBasicBattObj.setWifihastransport(wifihastransport);
        perfBasicBattObj.setCellularhastransport(cellularhastransport);
        try {

            dataListener.onDataReceive(perfBasicBattObj);
            // data population is done and available to other files via DataListerner
            // interfaces's onDataReceive function
        }
        catch (NullPointerException e){
            Log.e("NullPointerException","Caught Null Pointer Exception");
        }

        return perfBasicBattObj;
    } // Battery status data is populated - end

} // Batterybasicdetails class ends here
