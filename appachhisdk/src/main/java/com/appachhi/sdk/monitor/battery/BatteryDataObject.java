package com.appachhi.sdk.monitor.battery;

public class BatteryDataObject {

    private int processid;
    private int userid;
    private String perfDeviceOrientationText;
    private String perfScreenStatusText;
    private float perfRefreshrate;
    private int rotation;
    private StringBuilder perfTasklist;
    private StringBuilder perfProcesslist;
    private StringBuilder perfRecentTasklist;
    private String perfBatteryTechnology;
    private String perfChargingStatusText;
    private float perfBatteryTemperature;
    private float perfBatteryVoltage;
    private long perfBatteryCapacity;
    private float perfBatteryChargingRate;
    private String perfBatteryHealthLabel;
    private String perfPluggedInSource;
    private float perfBatteryPercentage;

    public float getPerfBatteryPercentage() {
        return perfBatteryPercentage;
    }

    public void setPerfBatteryPercentage(float perfBatteryPercentage) {
        this.perfBatteryPercentage = perfBatteryPercentage;
    }

    public String getPerfPluggedInSource() {
        return perfPluggedInSource;
    }

    public void setPerfPluggedInSource(String perfPluggedInSource) {
        this.perfPluggedInSource = perfPluggedInSource;
    }

    public String getPerfBatteryHealthLabel() {
        return perfBatteryHealthLabel;
    }

    public void setPerfBatteryHealthLabel(String perfBatteryHealthLabel) {
        this.perfBatteryHealthLabel = perfBatteryHealthLabel;
    }

    public String getPerfBatteryTechnology() {
        return perfBatteryTechnology;
    }

    public void setPerfBatteryTechnology(String perfBatteryTechnology) {
        this.perfBatteryTechnology = perfBatteryTechnology;
    }

    public String getPerfChargingStatusText() {
        return perfChargingStatusText;
    }

    public void setPerfChargingStatusText(String perfChargingStatusText) {
        this.perfChargingStatusText = perfChargingStatusText;
    }

    public float getPerfBatteryTemperature() {
        return perfBatteryTemperature;
    }

    public void setPerfBatteryTemperature(float perfBatteryTemperature) {
        this.perfBatteryTemperature = perfBatteryTemperature;
    }

    public float getPerfBatteryVoltage() {
        return perfBatteryVoltage;
    }

    public void setPerfBatteryVoltage(float perfBatteryVoltage) {
        this.perfBatteryVoltage = perfBatteryVoltage;
    }

    public long getPerfBatteryCapacity() {
        return perfBatteryCapacity;
    }

    public void setPerfBatteryCapacity(long perfBatteryCapacity) {
        this.perfBatteryCapacity = perfBatteryCapacity;
    }

    public float getPerfBatteryChargingRate() {
        return perfBatteryChargingRate;
    }

    public void setPerfBatteryChargingRate(float perfBatteryChargingRate) {
        this.perfBatteryChargingRate = perfBatteryChargingRate;
    }

    public StringBuilder getPerf_RecentTaskList() {
        return perfRecentTasklist;
    }

    public void setPerf_RecentTaskList(StringBuilder perf_RecentTaskString) {
        perfRecentTasklist = perf_RecentTaskString;
    }

    public StringBuilder getPerfProcesslist() {
        return perfProcesslist;
    }

    public void setPerfProcesslist(StringBuilder perfProcesslist) {
        this.perfProcesslist = perfProcesslist;
    }

    public StringBuilder getPerftasklist() {
        return perfTasklist;
    }

    public void setPerftasklist(StringBuilder perftasklist) {
        perfTasklist = perftasklist;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public String getScreenstatustext() {
        return perfScreenStatusText;
    }

    public void setScreenstatustext(String screenstatustext) {
        this.perfScreenStatusText = screenstatustext;
    }

    public float getPerfRefreshrate() {
        return perfRefreshrate;
    }

    public void setPerfRefreshrate(float perfRefreshrate) {
        this.perfRefreshrate = perfRefreshrate;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getPerfDeviceOrientationText() {
        return perfDeviceOrientationText;
    }

    public void setPerfDeviceOrientationText(String perfDeviceOrientationText) {
        this.perfDeviceOrientationText = perfDeviceOrientationText;
    }
}
