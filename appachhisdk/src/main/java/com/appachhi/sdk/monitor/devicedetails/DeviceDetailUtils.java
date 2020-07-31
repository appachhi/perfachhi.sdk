package com.appachhi.sdk.monitor.devicedetails;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import java.text.DecimalFormat;
import static java.lang.StrictMath.round;

public class DeviceDetailUtils {

    String modelid = null;
    String manufacturer;
    String softwareversion;
    String deviceid;
    int releaseid;
    float lcddensity;
    int screenheight;
    int screenwidth;
    String cpuarchitecture;
    String secureID = null;
    double raminfoTotalsize = 0;
    double raminfoRemainingsize = 0;
    String perfBTname = "";
    final double MB_VALUE = 1048576.0;
    float xcord, ycord, perfDensitydpi = 0, perfScreenSize;
    int processid;
    int userid;
    float perfRefreshrate;

    /* This function retrieves all device parameters via Android framework calls.
     * These values are bundled and sent via an Intent to the activity which will
     * access these values and stores in DB */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public DeviceDataObject fetchDeviceData(final Context context) { /* Fetch device data - Start */
        DeviceDataObject deviceobj = new DeviceDataObject();

        perfBTname = perfBTnamefunc();

        /* If value returned by the framework is for Device Model NULL. Store it as NA so that the activity
         * that makes use of these values can store it that way in DB. The values for the features
         * listed below are retrieved via calls which are less than 3 lines, hence they are maintained
         * as inline, rather than creating a seperate function for those*/
        try {
            if ((Build.MODEL) == null) {
                modelid = "NA";
                System.out.println("Device model returned NULL");
            } else {
                modelid = Build.MODEL;
            }
        } catch (NoSuchMethodError e) {
            Log.e("NoSuchMethod", "Method not found");
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
            perfDensitydpi = context.getResources().getDisplayMetrics().densityDpi;

        } else {
            lcddensity = 0;
            perfDensitydpi=0;
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

        secureID = secureIDFunc(context);
        perfScreenSize = perfScreenSizeFunc(screenwidth,screenheight,perfDensitydpi);

        raminfoTotalsize = raminfoTotalsizeFunc(context);
        raminfoRemainingsize = raminfoRemainingsizeFunc(context);

        processid = android.os.Process.myPid();
        userid = android.os.Process.myUid();
        perfRefreshrate = perfRefreshrateFunc(context);

        /* Invoke set method defined in deviceobject to set the devicedetails values */
        deviceobj.setmodelid(modelid);
        deviceobj.setmanufacturer(manufacturer);
        deviceobj.setlcddensity(perfDensitydpi);
        deviceobj.setScreenheight(screenheight);
        deviceobj.setscreenwidth(screenwidth);
        deviceobj.setreleaseid(releaseid);
        deviceobj.setdeviceid(deviceid);
        deviceobj.setsdklevel(releaseid);
        deviceobj.setsoftwareversion(softwareversion);
        deviceobj.setCPUarchitecture(cpuarchitecture);
        deviceobj.setSecure_ID(secureID);
        deviceobj.setBtname(perfBTname);
        deviceobj.setRaminfo_remainingsize(raminfoRemainingsize);
        deviceobj.setRaminfo_totalsize(raminfoTotalsize);
        deviceobj.setPerfScreenSize(perfScreenSize);
        deviceobj.setPerfRefreshrate(perfRefreshrate);
        deviceobj.setProcessid(processid);
        deviceobj.setUserid(userid);

        /*Return the object after populating it. This object will be accessed in Mainactivity*/
        return deviceobj;
    } /* Fetch device data - End */

    /* Retrieve Remaining RAM size - Start */
    private double raminfoRemainingsizeFunc(Context context){

        try {
            ActivityManager perfActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert perfActivityManager != null;
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            assert memoryInfo != null;
            perfActivityManager.getMemoryInfo(memoryInfo);
            DecimalFormat perfdecimal = new DecimalFormat("0.00");
            raminfoRemainingsize = memoryInfo.availMem / MB_VALUE;
            raminfoRemainingsize = Double.valueOf(perfdecimal.format(raminfoRemainingsize));
        }catch (NumberFormatException e){
            Log.e("NumberFormatException", "Number Format Exception");
        }
        catch (NullPointerException e){
            Log.e("NullPointerException","Null Pointer Exception");
        }
        catch (Exception e) {
            Log.e("General Exception", "An exception occured");
        }

        return raminfoRemainingsize;
    } /* Retrieve Remaining RAM size - End */

    /* Retrieve Total RAM size - Start */
    private double raminfoTotalsizeFunc(Context context){
        try {
            ActivityManager perfActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert perfActivityManager != null;
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            assert memoryInfo != null;
            perfActivityManager.getMemoryInfo(memoryInfo);
            DecimalFormat perfdecimal = new DecimalFormat("0.00");
            raminfoTotalsize = memoryInfo.totalMem / MB_VALUE;
            raminfoTotalsize = Double.valueOf(perfdecimal.format(raminfoTotalsize));

        }catch (NumberFormatException e){
            Log.e("NumberFormatException", "Number Format Exception");
        }
        catch (NullPointerException e){
            Log.e("NullPointerException","Null Pointer Exception");
        }
        catch (Exception e) {
            Log.e("General Exception", "An exception occured");
        }
        return raminfoTotalsize;
    } /* Retrieve Total RAM size - End */


    /* Get BTname - Start*/
    private String perfBTnamefunc(){

        BluetoothAdapter perf_BTAdapter = BluetoothAdapter.getDefaultAdapter();
        assert perf_BTAdapter != null;
        try {

            if (perf_BTAdapter == null) {
                //Bluetooth not supported
                perfBTname = "NA";
            } else if (perf_BTAdapter.isEnabled()) {
                //Bluetooth is supported and enabled
                perfBTname = perf_BTAdapter.getName();
                assert perfBTname != null;

                if (perfBTname == null) {
                    perfBTname = "NA";
                }
            } else {
                //Bluetooth is supported but disabled
                perfBTname = perf_BTAdapter.getName();
                assert perfBTname != null;
                if (perfBTname == null) {
                    perfBTname = "NA";
                }
            }
        } catch (NoSuchMethodError error) {
            Log.e("NoSuchmethodError", "No such method");
        } catch (Exception e) {
            Log.e("GenericException", "Exception Occured in WiFi enabled call");
        }
        return perfBTname;
    } /* Get BTname - End */

    /* Get Screen refresh rate via windows service - Start */
    private float perfRefreshrateFunc(Context context){
       try{
           Display perfDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
           assert perfDisplay != null;
           perfRefreshrate = perfDisplay.getRefreshRate();
       }catch (NullPointerException e){
           Log.e("NullPointerException","Null Pointer Exception");
       }catch (Exception e){
           Log.e("General Exception","General Exception");
       }
        return perfRefreshrate;
    } /* Get Screen refresh rate via windows service - End */

    /* Calculate Screen Size using Screen's width, height and Pixel density - Start */
    private float perfScreenSizeFunc(int screenwidth,int screenheight,float perfDensitydpi){

        try {
            DisplayMetrics perfDisplayMetrics = new DisplayMetrics();
            DecimalFormat perfdecimal = new DecimalFormat("0.00");

            xcord = (float) Math.pow(screenwidth, 2);
            ycord = (float) Math.pow(screenheight, 2);

            perfScreenSize = (float) Math.sqrt(xcord + ycord) / perfDensitydpi;
            perfScreenSize = Float.valueOf(perfdecimal.format(perfScreenSize));
        }catch (NullPointerException e){
            Log.e("NullPointerException","Null Pointer Exception Occured");
        }catch (NumberFormatException e){
            Log.e("NumberFormatException","Number Format Exception Occured");
        }catch (Exception e){
            Log.e("GeneralException","General Exception Occured");
        }
        return perfScreenSize;
    } /* Calculate Screen Size using Screen's width, height and Pixel density - End */

    /* Retrieve Android Secure ID from Device - Start */
    private String secureIDFunc(Context context){
       try {
           secureID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
           assert secureID != null;

           if (secureID == null) {
               secureID = "NA";
           }
       }catch (NullPointerException e){
           Log.e("NullPointerException","Null Pointer Exception");
       }catch (NoSuchMethodError e) {
           Log.e("NoSuchMethodError","No Such Method");
       }catch (Exception e){
           Log.e("General Exception","General Exception");
       }

        return secureID;
    } /* Retrieve Android Secure ID from Device - End */

}

