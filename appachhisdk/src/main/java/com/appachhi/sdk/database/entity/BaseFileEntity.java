package com.appachhi.sdk.database.entity;


import com.google.gson.annotations.SerializedName;

public class BaseFileEntity extends BaseEntity {
    @SerializedName("fileName")
    private String fileName;

    @SerializedName("filePath")
    private String filePath;

    @SerializedName("mimeType")
    private String mimeType;

    BaseFileEntity(String sessionId, long sessionTime, String fileName, String filePath, String mimeType) {
        super(sessionId, sessionTime);
        this.fileName = fileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
