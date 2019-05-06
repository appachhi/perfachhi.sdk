package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.GCEntity;

import java.util.List;

@Dao
public interface GCDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertGCRunInfo(GCEntity... gcRuns);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insetGcRunInfo(GCEntity gcRun);

    @Query("SELECT * FROM gc WHERE session_id = :sessionId")
    public List<GCEntity> allGCRunForTheSession(String sessionId);

    @Query("SELECT * FROM gc")
    public List<GCEntity> allGCRun();

    @Delete()
    public void deleteGcRuns(GCEntity gcEntity);
}
