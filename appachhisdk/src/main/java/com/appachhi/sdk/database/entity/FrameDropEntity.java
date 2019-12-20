package com.appachhi.sdk.database.entity;

import com.google.gson.annotations.SerializedName;

public class FrameDropEntity extends BaseEntity {
    @SerializedName("dropped")
    private int dropped;

    public FrameDropEntity(double dropped, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.dropped = (int) dropped;
    }

    public FrameDropEntity() {
    }


    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
        this.dropped = dropped;
    }
}
