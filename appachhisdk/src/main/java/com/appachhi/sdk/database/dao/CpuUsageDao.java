package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.CpuUsageEntity;

import java.util.List;

@Dao()
public interface CpuUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCpuUsages(CpuUsageEntity... cpuUsages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertCpuUsage(CpuUsageEntity cpuUsages);

    @Query("SELECT * FROM cpu_usage WHERE session_id = :sessionId")
    public List<CpuUsageEntity> allCpuUsageForTheSession(String sessionId);

    @Query("SELECT * FROM cpu_usage")
    public List<CpuUsageEntity> allCpuUsage();

    @Delete()
    public void deleteCpuUsage(CpuUsageEntity cpuUsageEntity);
}
