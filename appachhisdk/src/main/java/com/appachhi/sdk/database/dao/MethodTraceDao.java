package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.MethodTraceEntity;

import java.util.List;

@Dao
public interface MethodTraceDao {
    @Insert
    long addTrace(MethodTraceEntity methodTraceEntity);

    @Query("SELECT * FROM method_trace where syncStatus = 0 AND session_id IN (:sessionIds) limit 100")
    List<MethodTraceEntity> allUnSyncedMethodTraceEntityForSession(List<String> sessionIds);

    @Query("UPDATE method_trace SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete
    void deleteMethodTrace(MethodTraceEntity methodTraceEntity);

    @Query("Select * from method_trace")
    List<MethodTraceEntity> allMethodTrace();
}
