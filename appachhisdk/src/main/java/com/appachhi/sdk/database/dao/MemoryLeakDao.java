package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.MemoryLeakEntity;

import java.util.List;

@Dao
public interface MemoryLeakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMemoryLeak(MemoryLeakEntity gcRun);

    @Query("SELECT * FROM memory_leak WHERE session_id = :sessionId")
    public List<MemoryLeakEntity> allMemoryLeakForTheSession(String sessionId);

    @Query("SELECT * FROM memory_leak where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    public List<MemoryLeakEntity> oldest200UnSyncedMemoryLeak();

    @Query("UPDATE memory_leak SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM memory_leak")
    public List<MemoryLeakEntity> allMemoryLeak();

    @Delete()
    public void deleteMemoryLeak(MemoryLeakEntity gcEntity);
}
