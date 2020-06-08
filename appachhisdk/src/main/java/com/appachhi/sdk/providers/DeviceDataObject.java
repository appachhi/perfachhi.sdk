package com.appachhi.sdk.providers;

/* Data Object model for devicedetails. Parameters and Methods of devicedetails
*  Get and Set method for all parameters added here via getter and setter option */
public class DeviceDataObject {

    private int sdklevel;
    private int screenheight;
    private int screenwidth ;
    private String manufacturer;
    private  String softwareversion ;
    private String deviceid ;
    private int releaseid;
    private String CPUarchitecture;
    private String modelid;
    private float lcddensity;
    private String Secure_ID;
    private int processid;
    private int userid;

    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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
        return lcddensity;
    }

    public void setlcddensity(float lcddensity) {
        this.lcddensity = lcddensity;
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
