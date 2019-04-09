package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "network_usage", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id"),
        indices = {@Index(name = "network_usage_session_index", value = "session_id")})
public class NetworkUsageEntity extends BaseEntity {
    @ColumnInfo(name = "data_sent")
    private long dataSent;
    @ColumnInfo(name = "data_received")
    private long dataReceived;

    @Ignore
    public NetworkUsageEntity(long dataSent, long dataReceived, String sessionId) {
        super(sessionId);
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
