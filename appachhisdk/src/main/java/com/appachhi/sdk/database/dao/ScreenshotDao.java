package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.ScreenshotEntity;

import java.util.List;

@Dao
public interface ScreenshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertScreenshots(ScreenshotEntity... screenshotEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertScreenshot(ScreenshotEntity screenshotEntity);

    @Query("SELECT * FROM screenshot where syncStatus = 0 AND session_id in (:sessionId) limit 20")
    List<ScreenshotEntity> allUnSyncedScreenshotEntityForSession(List<String> sessionId);

    @Query("UPDATE screenshot SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM screenshot")
    public List<ScreenshotEntity> allScreenshots();

    @Delete()
    public void deleteScreenshot(ScreenshotEntity screenshotEntity);
}
