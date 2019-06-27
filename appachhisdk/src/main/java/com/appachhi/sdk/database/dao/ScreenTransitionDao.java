package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.TransitionStatEntity;

import java.util.List;

@Dao
public interface ScreenTransitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertScreenTranData(TransitionStatEntity transitionStatEntity);

    @Query("SELECT * FROM screen_transition where syncStatus = 0 AND session_id in (:sessionIds)")
    List<TransitionStatEntity> allUnSyncedScreenTransitionForSession(List<String> sessionIds);

    @Query("UPDATE screen_transition SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Delete()
    void deleteScreenTransitionData(TransitionStatEntity transitionStatEntity);
}
