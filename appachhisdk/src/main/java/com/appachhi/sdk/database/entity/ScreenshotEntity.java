package com.appachhi.sdk.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "screenshot", foreignKeys = @ForeignKey(
        entity = Session.class,
        parentColumns = "id",
        childColumns = "session_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(name = "screenshot_session_index", value = "session_id")})
public class ScreenshotEntity extends BaseFileEntity {

    public ScreenshotEntity(String sessionId, long sessionTime, String fileName, String filePath, String mimeType) {
        super(sessionId, sessionTime, fileName, filePath, mimeType);
    }
}
