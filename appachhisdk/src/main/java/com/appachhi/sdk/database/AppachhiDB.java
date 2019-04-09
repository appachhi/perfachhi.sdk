package com.appachhi.sdk.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.database.dao.MethodTraceDao;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.CpuUsageEntity;
import com.appachhi.sdk.database.entity.FpsEntity;
import com.appachhi.sdk.database.entity.GCEntity;
import com.appachhi.sdk.database.entity.MemoryEntity;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;
import com.appachhi.sdk.database.entity.MethodTraceEntity;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.database.entity.TransitionStatEntity;

@Database(entities = {
        CpuUsageEntity.class,
        Session.class,
        MemoryEntity.class,
        GCEntity.class,
        NetworkUsageEntity.class,
        FpsEntity.class,
        TransitionStatEntity.class,
        MemoryLeakEntity.class,
        MethodTraceEntity.class,
        APICallEntity.class}, version = 1)
public abstract class AppachhiDB extends RoomDatabase {
    private static final String DB_NAME = "appachhi";

    public abstract SessionDao sessionDao();

    public abstract CpuUsageDao cpuUsageDao();

    public abstract MemoryDao memoryDao();

    public abstract GCDao gcDao();

    public abstract FpsDao fpsDao();

    public abstract NetworkDao networkDao();

    public abstract MemoryLeakDao memoryLeakDao();

    public abstract ScreenTransitionDao screenTransitionDao();

    public abstract MethodTraceDao methodTraceDao();

    public abstract APICallDao apiCallDao();

    public static AppachhiDB create(Context context) {
        return Room.databaseBuilder(context, AppachhiDB.class, DB_NAME)
                .build();
    }

}

