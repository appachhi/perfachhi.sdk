package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.NetworkUsageEntity;

import java.util.List;

@Dao
public interface NetworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertNetworkUsages(NetworkUsageEntity... cpuUsages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertNetworkUsage(NetworkUsageEntity cpuUsages);

    @Query("SELECT * FROM network_usage where syncStatus = 0 AND session_id in (:sessionId) limit 100")
    List<NetworkUsageEntity> allUnSyncedNetworkUsageEntityForSession(List<String> sessionId);

    @Query("UPDATE network_usage SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteNetworkUsage(NetworkUsageEntity cpuUsageEntity);
}
