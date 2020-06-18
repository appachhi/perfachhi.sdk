package com.appachhi.sdk.monitor.battery;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;


import com.appachhi.sdk.monitor.devicedetails.DeviceDataObject;
import com.appachhi.sdk.monitor.devicedetails.DeviceDetailUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/* This class contains snippets for extracting details of device state for determining
*  battery use ( following are the values extracted: Screen refresh rate, device orientation
*  User id, Screen status, Process list, Task list, Recent task info */
public class BatteryDetailUtils {

    @SuppressLint("NewApi")

    public BatteryDataObject fetchBatteryData(final Context context){

        BatteryDataObject perfBattObj = new BatteryDataObject();
        int processid;
        int userid;
        String perfDeviceOrientationText = "";
        int perfDevicerotation;
        int screenstatus;
        String perfScreenStatusText="";

        /* Creat an instance of Device details */
        DeviceDetailUtils deviceutils = new DeviceDetailUtils();
        DeviceDataObject deviceData = deviceutils.fetchDeviceData(context);

        processid = android.os.Process.myPid();
        userid = android.os.Process.myUid();

        /* Get Screen refresh rate via windows service*/
        Display perfDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        assert perfDisplay != null;
        float perfRefreshrate = perfDisplay.getRefreshRate();

        /* Get Screen display status via display service - Start */
        DisplayManager perf_displayservice = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        assert perf_displayservice != null;

        for(Display display : perf_displayservice.getDisplays()) {
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
        }/* Get Screen display status via display service - End */


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


        /* Fetch Process list - Start*/
        final ActivityManager perfListofactivities = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert perfListofactivities != null;
        final List<ActivityManager.RunningAppProcessInfo> perfRunningProcessinfo = perfListofactivities.getRunningAppProcesses();
        assert perfRunningProcessinfo != null;
        StringBuilder perfAppProcessString = new StringBuilder();
        StringBuilder perfTaskActivitiesString = new StringBuilder();
        StringBuilder perfRecentTaskString = new StringBuilder();

        if(perfRunningProcessinfo != null)
        {
            for(int i=0;i<perfRunningProcessinfo.size(); i++){

                perfAppProcessString.append(perfRunningProcessinfo.get(i).toString() + "\n");

            }
        } /* Fetch Process list - End */

        /* Fetching Recent Task info - End */
        ActivityManager perfActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RecentTaskInfo> perfRecentTaskList = new ArrayList<>(perfActivityManager.getRecentTasks(Integer.MAX_VALUE,
                ActivityManager.RECENT_WITH_EXCLUDED | ActivityManager.RECENT_IGNORE_UNAVAILABLE));
        ListIterator<ActivityManager.RecentTaskInfo> perfRecentTaskListIterator = perfRecentTaskList.listIterator();
        while(perfRecentTaskListIterator.hasNext()) {
            ActivityManager.RecentTaskInfo perfRecentTaskInfo = perfRecentTaskListIterator.next();
            perfRecentTaskString.append(perfRecentTaskInfo);
        }
        /* Fetching Recent Task info - End */

        /* Fetch Task list - Start*/
        List perfRunningAppTasks = perfListofactivities.getAppTasks();
        assert  perfRunningAppTasks != null;

        if(perfRunningAppTasks != null){

            for(int i=0;i < perfRunningAppTasks.size();i++){
                perfTaskActivitiesString.append(perfRunningAppTasks.get(i).toString());
            }
        } /* Fetch Task list - End */


        /* Populating Battery object with details - Start */
        perfBattObj.setPerfDeviceOrientationText(perfDeviceOrientationText);
        perfBattObj.setRotation(perfDevicerotation);
        perfBattObj.setPerfRefreshrate(perfRefreshrate);
        perfBattObj.setScreenstatustext(perfScreenStatusText);
        perfBattObj.setProcessid(processid);
        perfBattObj.setUserid(userid);
        perfBattObj.setPerftasklist(perfTaskActivitiesString);
        perfBattObj.setPerfProcesslist(perfAppProcessString);
        perfBattObj.setPerf_RecentTaskList(perfRecentTaskString);
        /* Populating Battery object with details - End */

        /* Return the BatteryObject after populating. Will be used in the main activity to store
        * values in the backend */
        return perfBattObj;
    }
}
