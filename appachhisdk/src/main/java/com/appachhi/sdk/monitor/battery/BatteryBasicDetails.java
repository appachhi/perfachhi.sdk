package com.appachhi.sdk.monitor.battery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;



import javax.security.auth.DestroyFailedException;

public class BatteryBasicDetails {
    private long perfBatteryCapacity;
    private float perfBatteryVoltage;
    private float perfBatteryTemperature;
    private float perfBatteryChargingRate;
    private String perfBatteryTechnology;
    private String perfChargingStatusText;
    private String perfBatteryHealthLabel;
    private DataListener dataListener;
    private String perfPluggedInSource;
    private float perfBatteryPercentage;

    @SuppressLint("SetTextI18n")
   // @RequiresApi(api = Build.VERSION_CODES.M)
    public void fetchbatterydetails(Activity context) {
        // Fetch Battery details - Start
        dataListener = (DataListener) context;
        IntentFilter batteryintent = new IntentFilter();
        batteryintent.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryintent.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryintent.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryintent.addAction(Intent.ACTION_BATTERY_OKAY);
        batteryintent.addAction(Intent.ACTION_BATTERY_LOW);
        batteryintent.addAction(Intent.ACTION_POWER_USAGE_SUMMARY);
        batteryintent.addAction(Intent.ACTION_SCREEN_ON);
        batteryintent.addAction(Intent.ACTION_SCREEN_OFF);

        context.registerReceiver(batteryDataReciever, batteryintent);

    } // Fetch Battery details - End

    /* This function retrieves updated data of battery statistics in BatteryDataReceiver call
     *  based on the Action registered in the intent */
   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void updatebatterydata(Intent intent, Context context){  //Update battery data - start
        boolean perf_Batterypresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if (perf_Batterypresent) {
            StringBuilder batterystring = new StringBuilder();
            int batteryhealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            perfBatteryHealthLabel = "UNKNOWN";

            /* Based on the constants returned by batterymanager assign correct label*/
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

            int batterylevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int batteryscale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if ((batterylevel != -1) && (batteryscale != -1)) {
                perfBatteryPercentage = (float) ((batterylevel / (float) batteryscale) * 100f);
            }

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

            if (intent.getExtras() != null) {
                perfBatteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            }

            perfBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            if (perfBatteryTemperature > 0) {
                perfBatteryTemperature = (float) perfBatteryTemperature / 10f;
            }

            perfBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            perfBatteryCapacity = getBatteryCapacity(context);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                if(perf_batterymanager != null) {
                    long batterycurrentvoltage = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
                    perfBatteryChargingRate = (int) batterycurrentvoltage / 1000;
                    /* This code is not used as of now...
                    if (perfBatteryChargingRate < 0) {
                        batterystring.append("Discharge rate :" + perfBatteryChargingRate).append(" mA ");
                    } else {
                        batterystring.append("Charging rate :" + perfBatteryChargingRate).append(" mA ");
                    }
                   */
                }
            }

        } else //No battery detected....
        {
            Toast.makeText(context, "No Battery detected", Toast.LENGTH_LONG).show();
        }
        fetchbatterybasicdata(context);
    }// Update battery data - End

    //Get Battery Capacity - Start
    private long getBatteryCapacity (Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager perf_batterymanager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            if(perf_batterymanager != null) {
                Long bCapacity = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                Long bchargecounter = perf_batterymanager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
                if (bCapacity != null && bchargecounter != null) {
                    long batteryvalue = (long) ((float) bchargecounter / (float) bCapacity * 100f) / 1000;
                    return batteryvalue;
                }
            }
        }
        return 0;
    }//Get Battery Capacity - End

    /* Update the battery data upon receiving the broadcast */
    private BroadcastReceiver batteryDataReciever = new BroadcastReceiver() {
        //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
    /* Populate values of Battery object and place it in batterydataobject of
    *  onDataReceive in DataListener. Main activity will pick data from this interface */
  //  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        try {
            dataListener.onDataReceive(perfBasicBattObj);
        }
        catch (NullPointerException e){
            Log.e("NullPointerException","Caught Null Pointer Exception");
        }

        return perfBasicBattObj;
    }

}
