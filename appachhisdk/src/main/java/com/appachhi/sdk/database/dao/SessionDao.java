package com.appachhi.sdk.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appachhi.sdk.database.entity.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertSession(Session session);

    @Query("SELECT * FROM session")
    public List<Session> allSessions();

    @Delete
    public void deleteSession(Session session);

    @Query("DELETE from session")
    public void deleteAllSession();
}
