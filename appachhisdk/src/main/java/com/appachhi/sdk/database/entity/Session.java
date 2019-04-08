package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "session")
public class Session {
    @PrimaryKey
    @NonNull
    private String id;
    private String manufacturer;
    private String model;

    @Ignore
    public Session(String manufacturer, String model) {
        this();
        this.manufacturer = manufacturer;
        this.model = model;
    }

    @SuppressWarnings("WeakerAccess")
    public Session() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static Session create() {
        return new Session(Build.BRAND, Build.MODEL);
    }

}
