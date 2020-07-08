package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

public class BatteryEntity extends BaseEntity{

    @SerializedName("batteryPercentage")
    private String batteryPercentage;

    @SerializedName("batteryPluggedInStatus")
    private String batteryPluggedInStatus;

    @SerializedName("batteryDeviceConsumption")
    private String batteryDeviceConsumption;

    @SerializedName("batteryAppConsumption")
    private String batteryAppConsumption;

    @SerializedName("batteryTemperature")
    private String batteryTemperature;

    @SerializedName("batteryVoltage")
    private String batteryVoltage;

    @SerializedName("batteryDischargeRate")
    private String batteryDischargeRate;


    public BatteryEntity() {

    }

    public BatteryEntity(String batteryPercentage, String batteryPluggedInStatus, String batteryDeviceConsumption, String batteryAppConsumption, String batteryTemperature,
                         String batteryVoltage, String batteryDischargeRate, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
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
