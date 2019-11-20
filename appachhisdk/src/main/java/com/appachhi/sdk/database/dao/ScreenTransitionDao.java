package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("SELECT * FROM screen_transition")
    public List<TransitionStatEntity> allScreenTransition();

    @Delete()
    void deleteScreenTransitionData(TransitionStatEntity transitionStatEntity);
}
