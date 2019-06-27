package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
}
