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

    @Delete()
    public void deleteFps(FpsEntity fpsEntity);
}
