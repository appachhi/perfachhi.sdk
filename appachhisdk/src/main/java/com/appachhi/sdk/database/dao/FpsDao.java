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

    @Query("SELECT * FROM fps where syncStatus = 0 AND session_id in (:sessionId) limit 100")
    List<FpsEntity> allUnSyncedFpsEntityForSession(List<String> sessionId);

    @Query("UPDATE fps SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteFps(FpsEntity fpsEntity);
}
