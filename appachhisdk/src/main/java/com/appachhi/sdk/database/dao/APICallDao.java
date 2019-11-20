package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.APICallEntity;

import java.util.List;

@Dao
public interface APICallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertApiCalls(APICallEntity... apiCallEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertApiCall(APICallEntity apiCallEntity);

    @Query("SELECT * FROM api_call where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<APICallEntity> allUnSyncedApiCallEntityForSession(List<String> sessionIds);

    @Query("UPDATE api_call SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
  
    @Query("SELECT * FROM api_call")
    public List<APICallEntity> allApiCalls();

    @Delete()
    public void deleteApiCalls(APICallEntity apiCallEntity);
}
