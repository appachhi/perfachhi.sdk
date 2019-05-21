package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.APICallEntity;

import java.util.List;

@Dao
public interface APICallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertApiCalls(APICallEntity... apiCallEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertApiCall(APICallEntity apiCallEntity);

    @Query("SELECT * FROM api_call WHERE session_id = :sessionId")
    public List<APICallEntity> allApiCallsForTheSession(String sessionId);

    @Query("SELECT * FROM api_call where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    public List<APICallEntity> oldest200UnSyncedNetworkUsage();

    @Query("UPDATE api_call SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
  
    @Query("SELECT * FROM api_call")
    public List<APICallEntity> allApiCalls();

    @Delete()
    public void deleteApiCalls(APICallEntity apiCallEntity);
}
