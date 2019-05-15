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

    @Query("SELECT * FROM memory_usage")
    public List<MemoryEntity> allMemoryUsage();

    @Delete()
    public void deleteMemoryUsage(MemoryEntity cpuUsageEntity);
}
