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

    @Query("SELECT * FROM api_call where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<APICallEntity> allUnSyncedApiCallEntityForSession(List<String> sessionIds);

    @Query("UPDATE api_call SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
  
    @Query("SELECT * FROM api_call")
    public List<APICallEntity> allApiCalls();

    @Delete()
    public void deleteApiCalls(APICallEntity apiCallEntity);
}
