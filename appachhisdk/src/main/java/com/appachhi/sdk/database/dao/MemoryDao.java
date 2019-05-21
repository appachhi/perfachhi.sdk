package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.MemoryEntity;

import java.util.List;

@Dao
public interface MemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertMemoryUsages(MemoryEntity... memoryUsages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMemoryUsage(MemoryEntity memoryUsage);

    @Query("SELECT * FROM memory_usage WHERE session_id = :sessionId")
    public List<MemoryEntity> allMemoryUsageForTheSession(String sessionId);
  
    @Query("SELECT * FROM memory_usage where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    public List<MemoryEntity> oldest200UnSyncedMemory();

    @Query("UPDATE memory_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM memory_usage")
    public List<MemoryEntity> allMemoryUsage();

    @Delete()
    public void deleteMemoryUsage(MemoryEntity cpuUsageEntity);
}
