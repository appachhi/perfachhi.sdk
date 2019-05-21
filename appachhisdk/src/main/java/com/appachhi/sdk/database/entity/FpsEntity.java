package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fps", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "fps_session_index", value = "session_id")})
public class FpsEntity extends BaseEntity {
    @SerializedName("fps")
    private double fps;

    @Ignore
    public FpsEntity(double fps, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.fps = fps;
    }

    public FpsEntity() {
    }


    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

}
