package com.appachhi.sdk.providers;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.Arrays;


public class DeviceDetailUtils {

    /* This function retrieves all device parameters via Android framework calls.
     * These values are bundled and sent via an Intent to the activity which will
     * access these values and stores in DB */
    @SuppressLint({"NewApi", "HardwareIds"})
  //  @RequiresApi(api = Build.VERSION_CODES.M)
    public  DeviceDataObject fetchDeviceData(final Context context){
        DeviceDataObject obj=new DeviceDataObject();
        String modelid;
        String manufacturer;
        String softwareversion;
        String deviceid;
        int releaseid;
        float lcddensity;
        int screenheight;
        int screenwidth;
        String CPUarchitecture;
        String Secure_ID = null;
        int processid;
        int userid;

        processid = android.os.Process.myPid();
        userid = android.os.Process.myUid();

        /* If value returned by the framework is NULL. Store it as NA so that the activity
         * that makes use of these values can store it that way in DB.*/
        if((Build.MODEL) == null){

            modelid = "NA";
            System.out.println("Device model returned NULL");
        }
        else
        {
            modelid = Build.MODEL;
        }

        if(Build.MANUFACTURER == null){

            manufacturer = "NA";
            System.out.println("Manufacturer returned NULL");
        }
        else
        {
            manufacturer = Build.MANUFACTURER;
        }

        if(Build.VERSION.RELEASE == null){
            softwareversion = "NA";
        }
        else
        {
            softwareversion = Build.VERSION.RELEASE;
        }

        if(Build.DEVICE == null){
            deviceid = "NA";
        }
        else
        {
            deviceid = Build.DEVICE;
        }

        if(Build.VERSION.SDK_INT > 0)
        {
            releaseid = Build.VERSION.SDK_INT;
        }
        else
        {
            releaseid = 0;
        }

        if(Build.CPU_ABI != null)
        {
            CPUarchitecture = Build.CPU_ABI;
        }
        else
        {
            CPUarchitecture = "NA";
        }

        if(context.getResources().getDisplayMetrics().density > 0){
            lcddensity = context.getResources().getDisplayMetrics().density;
        }
        else
        {
            lcddensity = 0;
        }

        if(context.getResources().getDisplayMetrics().heightPixels > 0){
            screenheight = context.getResources().getDisplayMetrics().heightPixels;
        }
        else
        {
            screenheight = 0;
        }
        if(context.getResources().getDisplayMetrics().widthPixels > 0){
            screenwidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        else
        {
            screenwidth = 0;
        }

        Secure_ID = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);

       /* Invoke set method defined deviceobject to set the devicedetails values  */
        obj.setmodelid(modelid);
        obj.setmanufacturer(manufacturer);
        obj.setlcddensity(lcddensity);
        obj.setScreenheight(screenheight);
        obj.setscreenwidth(screenwidth);
        obj.setreleaseid(releaseid);
        obj.setdeviceid(deviceid);
        obj.setsdklevel(releaseid);
        obj.setsoftwareversion(softwareversion);
        obj.setCPUarchitecture(CPUarchitecture);
        obj.setSecure_ID(Secure_ID);
        obj.setProcessid(processid);
        obj.setUserid(userid);

        /*Return the object after populating it. This object will be accessed in Mainactivity*/
        return obj;
    }
}
