package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.CpuUsageEntity;

import java.util.List;

@Dao()
public interface CpuUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCpuUsages(CpuUsageEntity... cpuUsages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertCpuUsage(CpuUsageEntity cpuUsages);

    @Query("SELECT * FROM cpu_usage where syncStatus = 0 AND session_id in (:sessionId) limit 100")
    List<CpuUsageEntity> allUnSyncedCpuEntityForSession(List<String> sessionId);

    @Query("UPDATE cpu_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
    @Query("SELECT * FROM cpu_usage")
    public List<CpuUsageEntity> allCpuUsage();

    @Delete()
    public void deleteCpuUsage(CpuUsageEntity cpuUsageEntity);
}
