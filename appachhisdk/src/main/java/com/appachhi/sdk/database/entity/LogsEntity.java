package com.appachhi.sdk.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "log", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "log_session_index", value = "session_id")})
public class LogsEntity extends BaseFileEntity {

    public LogsEntity(String sessionId, long sessionTime, String fileName, String filePath) {
        super(sessionId, sessionTime, fileName, filePath, "text/plain");
    }

}
