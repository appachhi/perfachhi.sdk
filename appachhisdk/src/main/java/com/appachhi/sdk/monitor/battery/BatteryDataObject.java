package com.appachhi.sdk.monitor.battery;

public class BatteryDataObject {

    private String perfDeviceOrientationText;
    private String perfScreenStatusText;
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
    private String perfPowerSaveMode;
    private String perfLifeCycleState;
    private float perfBatteryAvailablemA;
    private String perfDateTime,gpsEnabled,perfNetworkType,perfBTState, perfWiFiState,cellularstatetext;
    private boolean btEnabled,wifiEnabled;
    private boolean bthastransport = false;
    private boolean wifihastransport = false;
    private boolean cellularhastransport = false;

    public boolean Wifihastransport() {
        return wifihastransport;
    }

    public void setWifihastransport(boolean wifihastransport) {
        this.wifihastransport = wifihastransport;
    }

    public boolean cellularhastransport() {
        return cellularhastransport;
    }

    public void setCellularhastransport(boolean cellularhastransport) {
        this.cellularhastransport = cellularhastransport;
    }

    public boolean bthastransport() {
        return bthastransport;
    }

    public void setBthastransport(boolean bthastransport) {
        this.bthastransport = bthastransport;
    }


    public String getCellularstatetext() {
        return cellularstatetext;
    }

    public void setCellularstatetext(String cellularstatetext) {
        this.cellularstatetext = cellularstatetext;
    }

    public String getPerfWiFiState() {
        return perfWiFiState;
    }

    public void setPerfWiFiState(String perfWiFiState) {
        this.perfWiFiState = perfWiFiState;
    }

    public String getPerfBTState() {
        return perfBTState;
    }

    public void setPerfBTState(String perfBTState) {
        this.perfBTState = perfBTState;
    }

    public boolean isWifiEnabled() {
        return wifiEnabled;
    }

    public void setWifiEnabled(boolean wifiEnabled) {
        this.wifiEnabled = wifiEnabled;
    }

    public boolean isBtEnabled() {
        return btEnabled;
    }

    public void setBtEnabled(boolean btEnabled) {
        this.btEnabled = btEnabled;
    }

    public String getPerfNetworkType() {
        return perfNetworkType;
    }

    public void setPerfNetworkType(String perfNetworkType) {
        this.perfNetworkType = perfNetworkType;
    }

    public String isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(String gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public String getPerfDateTime() {
        return perfDateTime;
    }

    public void setPerfDateTime(String perfDateTime) {
        this.perfDateTime = perfDateTime;
    }

    public float getPerfBatteryAvailablemA() {
        return perfBatteryAvailablemA;
    }

    public void setPerfBatteryAvailablemA(float perfBatteryAvailablemA) {
        this.perfBatteryAvailablemA = perfBatteryAvailablemA;
    }

    public String getPerfLifeCycleState() {
        return perfLifeCycleState;
    }

    public void setPerfLifeCycleState(String perfLifeCycleState) {
        this.perfLifeCycleState = perfLifeCycleState;
    }

    public String getPerfPowerSaveMode() {
        return perfPowerSaveMode;
    }

    public void setPerfPowerSaveMode(String perfPowerSaveMode) {
        this.perfPowerSaveMode = perfPowerSaveMode;
    }

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



    public String getScreenstatustext() {
        return perfScreenStatusText;
    }

    public void setScreenstatustext(String screenstatustext) {
        this.perfScreenStatusText = screenstatustext;
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
