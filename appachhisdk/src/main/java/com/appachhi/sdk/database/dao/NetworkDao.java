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

    @Query("SELECT * FROM network_usage WHERE session_id = :sessionId")
    public List<NetworkUsageEntity> allNetworkUsagesForTheSession(String sessionId);

    @Query("SELECT * FROM network_usage")
    public List<NetworkUsageEntity> allNetwork();

    @Delete()
    public void deleteNetworkUsage(NetworkUsageEntity cpuUsageEntity);
}
