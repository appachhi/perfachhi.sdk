package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.FpsEntity;

import java.util.List;

@Dao
public interface FpsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertFps(FpsEntity... fpsEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertFps(FpsEntity fpsEntity);

    @Query("SELECT * FROM fps where syncStatus = 0 AND session_id in (:sessionId) limit 100")
    List<FpsEntity> allUnSyncedFpsEntityForSession(List<String> sessionId);

    @Query("UPDATE fps SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
  
    @Query("SELECT * FROM fps")
    public List<FpsEntity> allFps();

    @Delete()
    public void deleteFps(FpsEntity fpsEntity);
}
