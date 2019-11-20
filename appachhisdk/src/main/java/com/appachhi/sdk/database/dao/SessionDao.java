package com.appachhi.sdk.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appachhi.sdk.database.entity.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSession(Session session);

    @Query("SELECT * FROM session")
    List<Session> allSessions();

    @Query("SELECT * FROM session where syncStatus=0")
    List<Session> allUnSyncedSessions();

    @Query("SELECT id FROM session where syncStatus=1")
    List<String> allSyncedSessionIds();

    @Delete
    void deleteSession(Session session);

    @Query("DELETE from session")
    void deleteAllSession();

    @Query("UPDATE session SET  syncStatus = 1 WHERE id IN (:ids)")
    void updateSuccessSyncStatus(List<String> ids);
}
