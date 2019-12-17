package com.appachhi.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.Session;

import java.util.List;

public class AppachhiDB {

    public AppachhiDB(SQLiteDatabase sqLiteDatabase) {
    }

    private static final String DB_NAME = "appachhi";

//    public abstract SessionDao sessionDao();
//
//    public abstract CpuUsageDao cpuUsageDao();
//
//    public abstract MemoryDao memoryDao();
//
//    public abstract GCDao gcDao();
//
//    public abstract FpsDao fpsDao();
//
//    public abstract NetworkDao networkDao();
//
//    public abstract MemoryLeakDao memoryLeakDao();
//
//    public abstract ScreenTransitionDao screenTransitionDao();
//
//    public abstract MethodTraceDao methodTraceDao();
//
//    public abstract APICallDao apiCallDao();
//
//    public abstract ScreenshotDao screenshotDao();
//
//    public abstract LogsDao logsDao();

    public static AppachhiDB create(Context context) {
        return new AppachhiDB(new AppachhiSqlOpenHelper(context).getWritableDatabase());
    }

}

class SessionDaoImpl implements SessionDao {

    @Override
    public long insertSession(Session session) {
        return 0;
    }

    @Override
    public List<Session> allSessions() {
        return null;
    }

    @Override
    public List<Session> allUnSyncedSessions() {
        return null;
    }

    @Override
    public List<String> allSyncedSessionIds() {
        return null;
    }

    @Override
    public void deleteSession(Session session) {

    }

    @Override
    public void deleteAllSession() {

    }

    @Override
    public void updateSuccessSyncStatus(List<String> ids) {

    }
}

