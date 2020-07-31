package com.appachhi.sdk.monitor.devicedetails;

/* Data Object model for devicedetails. Parameters and Methods of devicedetails
*  Get and Set method for all parameters added here via getter and setter option */
public class DeviceDataObject {

    private int sdklevel;
    private int screenheight;
    private int screenwidth ;
    private String manufacturer;
    private String softwareversion ;
    private String deviceid ;
    private int releaseid;
    private String CPUarchitecture;
    private String modelid;
    private float perfDensitydpi;
    private String Secure_ID;
    private double raminfo_totalsize;
    private double raminfo_remainingsize;
    private String btname;
    private boolean ethEnabled;
    private boolean wifiAwareEnabled;
    private float perfScreenSize;
    private String perfWiFiMac;
    private int processid;
    private int userid;
    private float perfRefreshrate;


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

    public float getPerfRefreshrate() {
        return perfRefreshrate;
    }

    public void setPerfRefreshrate(float perfRefreshrate) {
        this.perfRefreshrate = perfRefreshrate;
    }

    public String getPerfWiFiMac() {
        return perfWiFiMac;
    }

    public void setPerfWiFiMac(String perfWiFiMac) {
        this.perfWiFiMac = perfWiFiMac;
    }

    public float getPerfScreenSize() {
        return perfScreenSize;
    }

    public void setPerfScreenSize(float perfScreenSize) {
        this.perfScreenSize = perfScreenSize;
    }

    public boolean isEthEnabled() {
        return ethEnabled;
    }

    public void setEthEnabled(boolean ethEnabled) {
        this.ethEnabled = ethEnabled;
    }

    public boolean isWifiAwareEnabled() {
        return wifiAwareEnabled;
    }

    public void setWifiAwareEnabled(boolean wifiAwareEnabled) {
        this.wifiAwareEnabled = wifiAwareEnabled;
    }

    public String getBtname() {
        return btname;
    }

    public void setBtname(String btname) {
        this.btname = btname;
    }

    public double getRaminfo_totalsize() {
        return raminfo_totalsize;
    }

    public void setRaminfo_totalsize(double raminfo_totalsize) {
        this.raminfo_totalsize = raminfo_totalsize;
    }

    public double getRaminfo_remainingsize() {
        return raminfo_remainingsize;
    }

    public void setRaminfo_remainingsize(double raminfo_remainingsize) {
        this.raminfo_remainingsize = raminfo_remainingsize;
    }


    public String getSecure_ID() {
        return Secure_ID;
    }

    public void setSecure_ID(String secure_ID) {
        Secure_ID = secure_ID;
    }

    public String getmodelid() {
        return modelid;
    }

    public void setmodelid(String modelid) {
        this.modelid = modelid;
    }

    public float getlcddensity() {
        return perfDensitydpi;
    }

    public void setlcddensity(float perfDensitydpi) {
        this.perfDensitydpi = perfDensitydpi;
    }

    public int getScreenheight() {
        return screenheight;
    }

    public void setScreenheight(int screenheight) {
        this.screenheight = screenheight;
    }

    public int getscreenwidth() {
        return screenwidth;
    }

    public void setscreenwidth(int screenwidth) {
        this.screenwidth = screenwidth;
    }

    public String getmanufacturer() {
        return manufacturer;
    }

    public void setmanufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getsoftwareversion() {
        return softwareversion;
    }

    public void setsoftwareversion(String softwareversion) {
        this.softwareversion = softwareversion;
    }

    public String getdeviceid() {
        return deviceid;
    }

    public void setdeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getreleaseid() {
        return releaseid;
    }

    public void setreleaseid(int releaseid) {
        this.releaseid = releaseid;
    }

    public int getsdklevel() {
        return sdklevel;
    }

    public void setsdklevel(int sdklevel) {
        this.sdklevel = sdklevel;
    }

    public String getCPUarchitecture() {
        return CPUarchitecture;
    }

    public void setCPUarchitecture(String CPUarchitecture) {
        this.CPUarchitecture = CPUarchitecture;
    }

}
