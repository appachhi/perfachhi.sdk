package com.appachhi.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.dao.APICallDao;
import com.appachhi.sdk.database.dao.CpuUsageDao;
import com.appachhi.sdk.database.dao.FpsDao;
import com.appachhi.sdk.database.dao.FrameDropDao;
import com.appachhi.sdk.database.dao.GCDao;
import com.appachhi.sdk.database.dao.LogsDao;
import com.appachhi.sdk.database.dao.MemoryDao;
import com.appachhi.sdk.database.dao.MemoryLeakDao;
import com.appachhi.sdk.database.dao.MethodTraceDao;
import com.appachhi.sdk.database.dao.NetworkDao;
import com.appachhi.sdk.database.dao.ScreenTransitionDao;
import com.appachhi.sdk.database.dao.ScreenshotDao;
import com.appachhi.sdk.database.dao.SessionDao;


public class AppachhiDB {

    private static AppachhiDB instance;
    private final SessionDao sessionDao;
    private final CpuUsageDao cpuUsageDao;
    private final MemoryDao memoryDao;
    private final GCDao gcDao;
    private final ScreenshotDao screenshotDao;
    private final LogsDao logsDao;
    private final FpsDao fpsDao;
    private NetworkDao networkDao;
    private MemoryLeakDao memoryLeakDao;
    private APICallDao apiCallDao;
    private ScreenTransitionDao screenTransitionDao;
    private MethodTraceDao methodTraceDao;
    private FrameDropDao frameDropDao;

    private AppachhiDB(SQLiteDatabase sqLiteDatabase) {
        sessionDao = new SessionDao(sqLiteDatabase);
        cpuUsageDao = new CpuUsageDao(sqLiteDatabase);
        memoryDao = new MemoryDao(sqLiteDatabase);
        gcDao = new GCDao(sqLiteDatabase);
        screenshotDao = new ScreenshotDao(sqLiteDatabase);
        logsDao = new LogsDao(sqLiteDatabase);
        fpsDao = new FpsDao(sqLiteDatabase);
        networkDao = new NetworkDao(sqLiteDatabase);
        apiCallDao = new APICallDao(sqLiteDatabase);
        memoryLeakDao = new MemoryLeakDao(sqLiteDatabase);
        screenTransitionDao = new ScreenTransitionDao(sqLiteDatabase);
        methodTraceDao = new MethodTraceDao(sqLiteDatabase);
        frameDropDao = new FrameDropDao(sqLiteDatabase);
    }

    private static final String DB_NAME = "appachhi";

    public SessionDao sessionDao() {
        return sessionDao;
    }

    public CpuUsageDao cpuUsageDao() {
        return cpuUsageDao;
    }

    public MemoryDao memoryDao() {
        return memoryDao;
    }

    public GCDao gcDao() {
        return gcDao;
    }

    public FpsDao fpsDao() {
        return fpsDao;
    }

    public NetworkDao networkDao() {
        return networkDao;
    }

    public MemoryLeakDao memoryLeakDao() {
        return memoryLeakDao;
    }

    public ScreenTransitionDao screenTransitionDao() {
        return screenTransitionDao;
    }

    public MethodTraceDao methodTraceDao() {
        return methodTraceDao;
    }

    public APICallDao apiCallDao() {
        return apiCallDao;
    }

    public ScreenshotDao screenshotDao() {
        return screenshotDao;
    }

    public LogsDao logsDao() {
        return logsDao;
    }

    public FrameDropDao frameDropDao() {
        return frameDropDao;
    }

    public static AppachhiDB getInstance(Context context) {
        if (instance == null) {
            instance = new AppachhiDB(new AppachhiSqlOpenHelper(context).getWritableDatabase());
        }
        return instance;
    }

}
