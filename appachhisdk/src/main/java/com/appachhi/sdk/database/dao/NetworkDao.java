package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("Select * from network_usage")
    List<NetworkUsageEntity> allNetwork();
}
