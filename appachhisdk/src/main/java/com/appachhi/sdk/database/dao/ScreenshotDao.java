package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.ScreenshotEntity;

import java.util.List;

@Dao
public interface ScreenshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertScreenshots(ScreenshotEntity... screenshotEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertScreenshot(ScreenshotEntity screenshotEntity);

    @Query("SELECT * FROM screenshot where syncStatus = 0 AND session_id in (:sessionId) limit :limit")
    List<ScreenshotEntity> unSyncedScreenshotEntityForSession(List<String> sessionId, int limit);

    @Query("UPDATE screenshot SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM screenshot")
    public List<ScreenshotEntity> allScreenshots();

    @Delete()
    public void deleteScreenshot(ScreenshotEntity screenshotEntity);
}
