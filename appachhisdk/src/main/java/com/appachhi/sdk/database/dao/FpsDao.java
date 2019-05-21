package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.FpsEntity;

import java.util.List;

@Dao
public interface FpsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertFps(FpsEntity... fpsEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertFps(FpsEntity fpsEntity);

    @Query("SELECT * FROM fps WHERE session_id = :sessionId")
    public List<FpsEntity> allFpsForTheSession(String sessionId);

    @Query("SELECT * FROM fps where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    public List<FpsEntity> oldest200UnSyncedFps();

    @Query("UPDATE fps SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteFps(FpsEntity fpsEntity);
}
