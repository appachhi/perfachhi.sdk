package com.appachhi.sdk.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "network_usage", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id"),
        indices = {@Index(name = "network_usage_session_index", value = "session_id")})
public class NetworkUsageEntity extends BaseEntity {
    @ColumnInfo(name = "data_sent")
    @SerializedName("dataSent")
    private long dataSent;

    @ColumnInfo(name = "data_received")
    @SerializedName("dataReceived")
    private long dataReceived;

    @Ignore
    public NetworkUsageEntity(long dataSent, long dataReceived, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.dataSent = dataSent;
        this.dataReceived = dataReceived;
    }

    public NetworkUsageEntity() {
    }

    public long getDataSent() {
        return dataSent;
    }

    public void setDataSent(long dataSent) {
        this.dataSent = dataSent;
    }

    public long getDataReceived() {
        return dataReceived;
    }

    public void setDataReceived(long dataReceived) {
        this.dataReceived = dataReceived;
    }
}
