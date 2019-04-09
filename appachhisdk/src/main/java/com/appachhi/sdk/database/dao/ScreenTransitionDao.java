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
    public long insertScreenTranData(TransitionStatEntity transitionStatEntity);

    @Query("SELECT * FROM screen_transition WHERE session_id = :sessionId")
    public List<TransitionStatEntity> allScreenTransitionDataForTheSession(String sessionId);

    @Delete()
    public void deleteScreenTransitionData(TransitionStatEntity transitionStatEntity);
}
