package com.appachhi.sdk.monitor.battery.component;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.monitor.battery.BatteryBasicDetails;
import com.appachhi.sdk.monitor.battery.BatteryDataObject;
import com.appachhi.sdk.monitor.battery.DataListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.StrictMath.abs;

public class BatteryDataModule extends BaseDataModule<BatteryDataInfo> implements Runnable, DataListener {

    public BatteryDataInfo batteryDataInfo;
    public Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String TAG = "BatteryDataModule";
    public BatteryBasicDetails batteryBasicDetails;
  //  public BatteryDataObject batteryDataObject;

    private static float prevbatteryvalue, deviceBatteryConsumption;
    private static long prevtimeinmillisecs,timeinbackground=0,timeinforeground=0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryDataModule(Context context) {
        this.context = context;




        batteryBasicDetails = new BatteryBasicDetails();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // batteryBasicDetails.fetchbatterydetails(context);
            batteryBasicDetails.fetchbatterydetails(context);
            batteryBasicDetails.fetchbatterybasicdata(context);
            prevtimeinmillisecs = batteryBasicDetails.getPrevtime();

            batteryBasicDetails.setCustomObjectListener(this);


        } else {
            Log.d(TAG, "Constructor: from Battery data module ELSE statement. ");
        }

    }

    @Override
    protected BatteryDataInfo getData() {
        return batteryDataInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void start() {


//        handler.post(this);

    }

    @Override
    public void stop() {
  //      handler.removeCallbacks(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {


       /* batteryBasicDetails.setCustomObjectListener(new DataListener() {
            @Override
            public void onDataReceive(BatteryDataObject batteryDataObject) {

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                DecimalFormat perfdecimal = new DecimalFormat("00.000000000");
                long currenttimeinmillisec = cal.getTimeInMillis();
                long timediffinmillisec;
                String datetime = simpleDateFormat.format(currenttimeinmillisec);

                float dischargerate,batteryPercentage,appBatteryConsumption;
                dischargerate = batteryDataObject.getPerfBatteryChargingRate();
               // batteryPercentage = batteryDataObject.getPerfBatteryAvailablemA();
                batteryPercentage = batteryDataObject.getPerfBatteryPercentage();

                timediffinmillisec = currenttimeinmillisec - prevtimeinmillisecs;

                dischargerate = batteryDataObject.getPerfBatteryChargingRate();

                if(batteryDataObject.getPerfLifeCycleState() == "FOREGROUND"){
                    timeinforeground = timeinforeground + timediffinmillisec;

                }else{
                    timeinbackground = timeinbackground + timediffinmillisec;
                }

                if(dischargerate > 0){
                    deviceBatteryConsumption = batteryPercentage - prevbatteryvalue - dischargerate  ;
                    //batteryconsumption = batteryPercentage - prevbatteryvalue   ;
                }else{
                    deviceBatteryConsumption = prevbatteryvalue - batteryPercentage + dischargerate  ;
                    //batteryconsumption = prevbatteryvalue - batteryPercentage ;
                    //batteryconsumption = dischargerate;
                }

                deviceBatteryConsumption = abs(deviceBatteryConsumption)/timediffinmillisec;
                appBatteryConsumption = BatteryBasicDetails.getmyconsumption(deviceBatteryConsumption,batteryDataObject);
                appBatteryConsumption = Float.parseFloat(perfdecimal.format(appBatteryConsumption));

                float batteryTemperature = batteryDataObject.getPerfBatteryTemperature();
                float batteryVoltage = batteryDataObject.getPerfBatteryVoltage();

                String batterySource = batteryDataObject.getPerfPluggedInSource();

                batteryDataInfo = new BatteryDataInfo(String.valueOf(batteryPercentage), batterySource, String.valueOf(deviceBatteryConsumption), String.valueOf(appBatteryConsumption),
                        String.valueOf(batteryTemperature), String.valueOf(batteryVoltage), String.valueOf(dischargerate));

                // Battery Percentage, Device Consumption, App Consumption, Battery Temperature, Battery Voltage and Discharge rate

                prevbatteryvalue = batteryDataObject.getPerfBatteryAvailablemA();
                prevtimeinmillisecs = currenttimeinmillisec;


                Log.d(TAG, "onDataReceive: Value : " + batteryDataObject.getPerfPluggedInSource());
            }
        });
*/

        notifyObservers();
        handler.postDelayed(this, 1000);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDataReceive(BatteryDataObject batterydo) {

        getBatteryDetails(batterydo);

        notifyObservers();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getBatteryDetails(BatteryDataObject batteryDataObject) {


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        DecimalFormat perfdecimal = new DecimalFormat("00.000000000");
        long currenttimeinmillisec = cal.getTimeInMillis();
        long timediffinmillisec;
        String datetime = simpleDateFormat.format(currenttimeinmillisec);

        float dischargerate,batteryPercentage,appBatteryConsumption;
        dischargerate = batteryDataObject.getPerfBatteryChargingRate();
        // batteryPercentage = batteryDataObject.getPerfBatteryAvailablemA();
        batteryPercentage = batteryDataObject.getPerfBatteryPercentage();

        timediffinmillisec = currenttimeinmillisec - prevtimeinmillisecs;

        dischargerate = batteryDataObject.getPerfBatteryChargingRate();

        if(batteryDataObject.getPerfLifeCycleState() == "FOREGROUND"){
            timeinforeground = timeinforeground + timediffinmillisec;

        }else{
            timeinbackground = timeinbackground + timediffinmillisec;
        }

        if(dischargerate > 0){
            deviceBatteryConsumption = batteryPercentage - prevbatteryvalue - dischargerate  ;
            //batteryconsumption = batteryPercentage - prevbatteryvalue   ;
        }else{
            deviceBatteryConsumption = prevbatteryvalue - batteryPercentage + dischargerate  ;
            //batteryconsumption = prevbatteryvalue - batteryPercentage ;
            //batteryconsumption = dischargerate;
        }

        deviceBatteryConsumption = abs(deviceBatteryConsumption)/timediffinmillisec;
        appBatteryConsumption = BatteryBasicDetails.getmyconsumption(deviceBatteryConsumption,batteryDataObject);
        appBatteryConsumption = Float.parseFloat(perfdecimal.format(appBatteryConsumption));

        float batteryTemperature = batteryDataObject.getPerfBatteryTemperature();
        float batteryVoltage = batteryDataObject.getPerfBatteryVoltage();

        String batterySource = batteryDataObject.getPerfPluggedInSource();

        batteryDataInfo = new BatteryDataInfo(String.valueOf(batteryPercentage), batterySource, String.valueOf(deviceBatteryConsumption), String.valueOf(appBatteryConsumption),
                String.valueOf(batteryTemperature), String.valueOf(batteryVoltage), String.valueOf(dischargerate));

        // Battery Percentage, Device Consumption, App Consumption, Battery Temperature, Battery Voltage and Discharge rate

        prevbatteryvalue = batteryDataObject.getPerfBatteryAvailablemA();
        prevtimeinmillisecs = currenttimeinmillisec;



    }
}
