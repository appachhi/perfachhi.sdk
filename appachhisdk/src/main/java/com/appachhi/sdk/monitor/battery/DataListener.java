package com.appachhi.sdk.monitor.battery;


import android.content.Context;

/*  Whenever data is available for the intents registered.This gets loaded
 with data that can be pickedup by mainactivity */
public interface DataListener {
    public void onDataReceive(BatteryDataObject batteryDataObject);
}
