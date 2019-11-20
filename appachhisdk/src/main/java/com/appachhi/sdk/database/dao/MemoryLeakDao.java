package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.MemoryLeakEntity;

import java.util.List;

@Dao
public interface MemoryLeakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMemoryLeak(MemoryLeakEntity gcRun);

    @Query("SELECT * FROM memory_leak where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<MemoryLeakEntity> allUnSyncedMemoryLeakEntityForSession(List<String> sessionIds);

    @Query("UPDATE memory_leak SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM memory_leak")
    public List<MemoryLeakEntity> allMemoryLeak();

    @Delete()
    public void deleteMemoryLeak(MemoryLeakEntity gcEntity);
}
