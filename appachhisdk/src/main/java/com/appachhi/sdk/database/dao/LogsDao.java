package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.LogsEntity;

import java.util.List;

@Dao
public interface LogsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertLogss(LogsEntity... logEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertLogs(LogsEntity logEntity);

    @Query("SELECT * FROM log where syncStatus = 0 AND session_id in (:sessionId) limit :limit")
    List<LogsEntity> unSyncedLogEntityForSession(List<String> sessionId, int limit);

    @Query("UPDATE log SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM log")
    public List<LogsEntity> allLogs();

    @Delete()
    public void deleteLogs(LogsEntity logEntity);
}
