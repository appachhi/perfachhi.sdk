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

    @Query("SELECT * FROM screen_transition WHERE session_id = :sessionId")
    List<TransitionStatEntity> allScreenTransitionDataForTheSession(String sessionId);

    @Query("SELECT * FROM screen_transition where syncStatus = 0 ORDER BY execution_time ASC limit 200")
    List<TransitionStatEntity> oldest200UnSyncedTransitionStat();

    @Query("UPDATE screen_transition SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);

    @Query("SELECT * FROM screen_transition")
    public List<TransitionStatEntity> allScreenTransition();

    @Delete()
    void deleteScreenTransitionData(TransitionStatEntity transitionStatEntity);
}
