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

    @Query("SELECT * FROM cpu_usage where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    public List<CpuUsageEntity> oldest200UnSyncedCpuUsages();

    @Query("UPDATE cpu_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
    @Query("SELECT * FROM cpu_usage")
    public List<CpuUsageEntity> allCpuUsage();

    @Delete()
    public void deleteCpuUsage(CpuUsageEntity cpuUsageEntity);
}
