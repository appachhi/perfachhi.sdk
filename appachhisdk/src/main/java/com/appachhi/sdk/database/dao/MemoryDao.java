package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.MemoryEntity;

import java.util.List;

@Dao
public interface MemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertMemoryUsages(MemoryEntity... memoryUsages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMemoryUsage(MemoryEntity memoryUsage);

    @Query("SELECT * FROM memory_usage where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<MemoryEntity> allUnSyncedMemoryEntityForSession(List<String> sessionIds);

    @Query("UPDATE memory_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteMemoryUsage(MemoryEntity cpuUsageEntity);

    @Query("SELECT * from memory_usage")
    List<MemoryEntity> allMemoryUsage();
}
