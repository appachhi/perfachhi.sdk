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

    @Query("SELECT * FROM memory_usage where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<MemoryEntity> allUnSyncedMemoryEntityForSession(List<String> sessionIds);

    @Query("UPDATE memory_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteMemoryUsage(MemoryEntity cpuUsageEntity);
}
