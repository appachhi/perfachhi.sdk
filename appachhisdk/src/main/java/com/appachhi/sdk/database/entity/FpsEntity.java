package com.appachhi.sdk.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fps", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "fps_session_index", value = "session_id")})
public class FpsEntity extends BaseEntity {
    @SerializedName("fps")
    private int fps;

    @Ignore
    public FpsEntity(double fps, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.fps = (int) fps;
    }

    public FpsEntity() {
    }


    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

}
