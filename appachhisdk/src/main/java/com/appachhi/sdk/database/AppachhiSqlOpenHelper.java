package com.appachhi.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppachhiSqlOpenHelper extends SQLiteOpenHelper {
    AppachhiSqlOpenHelper(Context context) {
        super(context, "appachhi", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSessionTable = "CREATE TABLE IF NOT EXISTS sessions (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  startTime INT," +
                "  synStatus INT," +
                "  versionCode INT," +
                "  versionName TEXT," +
                "  packageName TEXT," +
                "  syncStatus INT" +
                ")";

        String createAPIEntryTable = "CREATE TABLE IF NOT EXISTS api_calls (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  url TEXT," +
                "  methodType INT," +
                "  contentType INT," +
                "  requestContentLength TEXT," +
                "  responseCode TEXT," +
                "  duration INT," +
                "  threadName TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createCpuUsageTable = "CREATE TABLE IF NOT EXISTS cpu_usages (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  appCpuUsage INT," +
                "  deviceCpuUsage INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";
        String createFpsTable = "CREATE TABLE IF NOT EXISTS fps (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  fps INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createFrameDropTable = "CREATE TABLE IF NOT EXISTS frame_drops (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  dropped INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createGcTable = "CREATE TABLE IF NOT EXISTS gc (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  gcName TEXT," +
                "   objectFreed TEXT," +
                "   objectFreedSize TEXT," +
                "   allocSpaceObjectFreed TEXT," +
                "   allocSpaceObjectFreedSize TEXT," +
                "   largeObjectFreedPercentage TEXT," +
                "   largeObjectFreedSize TEXT," +
                "   largeObjectTotalSize TEXT," +
                "   gcPauseTime TEXT, gcRunTime TEXT," +
                "  " +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createLogsTable = "CREATE TABLE IF NOT EXISTS logs (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  fileName TEXT," +
                "  filePath TEXT," +
                "  mimeType TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createScreenshotTable = "CREATE TABLE IF NOT EXISTS screenshots (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  fileName TEXT," +
                "  filePath TEXT," +
                "  mimeType TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createMemoryUsageTabel = "CREATE TABLE IF NOT EXISTS memory_usages (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  javaHeap INT," +
                "  nativeHeap INT," +
                "  summaryCode INT," +
                "  stack INT," +
                "  graphics INT," +
                "  privateOther INT," +
                "  summarySystem INT," +
                "  totalSwap INT," +
                "  threshold INT," +
                "  totalPSS INT," +
                "  totalPrivateDirty INT," +
                "  totalSharedDirty INT," +
                "  systemResourceMemory INT," +
                "  swapMemory INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";
        String createMemoryLeakTable = "CREATE TABLE IF NOT EXISTS memory_leaks (" +
                "  _id TEXT PRIMARY KEY NOT NULL," +
                "  className TEXT," +
                "  leakTrace TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";


        String createMethodTraceTable = "CREATE TABLE IF NOT EXISTS method_traces (" +
                "_id TEXT PRIMARY KEY NOT NULL," +
                "trace TEXT," +
                "traceName TEXT," +
                "duration INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";

        String createTransitionTable = "CREATE TABLE IF NOT EXISTS transition_stats (" +
                "_id TEXT PRIMARY KEY NOT NULL," +
                "name TEXT," +
                "duration INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";
        String createNetworkUsageTable = "CREATE TABLE IF NOT EXISTS network_usages (" +
                "_id TEXT PRIMARY KEY NOT NULL," +
                "dataReceived INT," +
                "dataSent INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";

        String createStartupTimeTable = "CREATE TABLE IF NOT EXISTS startup_time (" +
                "_id TEXT PRIMARY KEY NOT NULL," +
                "coldStartValue INT," +
                "warmStartValue INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";

        db.execSQL(createSessionTable);
        db.execSQL(createAPIEntryTable);
        db.execSQL(createCpuUsageTable);
        db.execSQL(createFpsTable);
        db.execSQL(createFrameDropTable);
        db.execSQL(createGcTable);
        db.execSQL(createLogsTable);
        db.execSQL(createScreenshotTable);
        db.execSQL(createMemoryLeakTable);
        db.execSQL(createMemoryUsageTabel);
        db.execSQL(createMethodTraceTable);
        db.execSQL(createTransitionTable);
        db.execSQL(createNetworkUsageTable);
        db.execSQL(createStartupTimeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}