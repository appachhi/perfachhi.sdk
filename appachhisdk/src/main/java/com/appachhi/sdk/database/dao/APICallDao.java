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

    @Delete()
    public void deleteApiCalls(APICallEntity apiCallEntity);
}
