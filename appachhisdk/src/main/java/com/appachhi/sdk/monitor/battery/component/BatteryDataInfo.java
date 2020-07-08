package com.appachhi.sdk.monitor.battery.component;

import com.google.gson.annotations.SerializedName;

public class BatteryDataInfo {

    public String batteryPercentage;
    public String batteryPluggedInStatus;
    public String batteryDeviceConsumption;
    public String batteryAppConsumption;
    public String batteryTemperature;
    public String batteryVoltage;
    public String batteryDischargeRate;


    public BatteryDataInfo() {
    }

    public BatteryDataInfo(String batteryPercentage, String batteryPluggedInStatus, String batteryDeviceConsumption, String batteryAppConsumption, String batteryTemperature, String batteryVoltage, String batteryDischargeRate) {
        this.batteryPercentage = batteryPercentage;
        this.batteryPluggedInStatus = batteryPluggedInStatus;
        this.batteryDeviceConsumption = batteryDeviceConsumption;
        this.batteryAppConsumption = batteryAppConsumption;
        this.batteryTemperature = batteryTemperature;
        this.batteryVoltage = batteryVoltage;
        this.batteryDischargeRate = batteryDischargeRate;
    }

    public String getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(String batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public String getBatteryPluggedInStatus() {
        return batteryPluggedInStatus;
    }

    public void setBatteryPluggedInStatus(String batteryPluggedInStatus) {
        this.batteryPluggedInStatus = batteryPluggedInStatus;
    }

    public String getBatteryDeviceConsumption() {
        return batteryDeviceConsumption;
    }

    public void setBatteryDeviceConsumption(String batteryDeviceConsumption) {
        this.batteryDeviceConsumption = batteryDeviceConsumption;
    }

    public String getBatteryAppConsumption() {
        return batteryAppConsumption;
    }

    public void setBatteryAppConsumption(String batteryAppConsumption) {
        this.batteryAppConsumption = batteryAppConsumption;
    }

    public String getBatteryTemperature() {
        return batteryTemperature;
    }

    public void setBatteryTemperature(String batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getBatteryDischargeRate() {
        return batteryDischargeRate;
    }

    public void setBatteryDischargeRate(String batteryDischargeRate) {
        this.batteryDischargeRate = batteryDischargeRate;
    }
}

