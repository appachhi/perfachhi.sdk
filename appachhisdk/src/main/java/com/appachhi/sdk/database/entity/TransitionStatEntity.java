package com.appachhi.sdk.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(tableName = "screen_transition", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "screen_transition_session_index", value = "session_id")})
public class TransitionStatEntity extends BaseEntity {
    private String name;
    private long duration;

    @Ignore
    public TransitionStatEntity(String name, long duration, String sessionId, long sessionTime) {
        super(sessionId, sessionTime);
        this.name = name;
        this.duration = duration;
    }

    public TransitionStatEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
