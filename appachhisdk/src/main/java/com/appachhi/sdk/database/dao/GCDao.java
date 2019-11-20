package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.GCEntity;

import java.util.List;

@Dao
public interface GCDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertGCRunInfo(GCEntity... gcRuns);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insetGcRunInfo(GCEntity gcRun);


    @Query("SELECT * FROM gc where syncStatus = 0 AND session_id in (:sessionIds) limit 100")
    List<GCEntity> allUnSyncedGcEntityForSession(List<String> sessionIds);

    @Query("UPDATE gc SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    public void deleteGcRuns(GCEntity gcEntity);

    @Query("SELECT * from gc")
    List<GCEntity> allGCRun();
}
