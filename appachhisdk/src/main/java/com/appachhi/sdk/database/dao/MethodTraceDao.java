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

    @Query("SELECT * from method_trace where session_id=:sessionId")
    List<MethodTraceEntity> allMethodTraceForTheSession(String sessionId);

    @Query("SELECT * from method_trace")
    List<MethodTraceEntity> allMethodTrace();

    @Delete
    void deleteMethodTrace(MethodTraceEntity methodTraceEntity);
}
