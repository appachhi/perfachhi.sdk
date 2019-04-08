package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "fps", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id"),
indices = {@Index("fps")})
public class FpsEntity extends BaseEntity {
    private double fps;

    @Ignore
    public FpsEntity(double fps, String sessionId) {
        super(sessionId);
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
