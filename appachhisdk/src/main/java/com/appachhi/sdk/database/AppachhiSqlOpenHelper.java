package com.appachhi.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppachhiSqlOpenHelper extends SQLiteOpenHelper {
    AppachhiSqlOpenHelper(Context context) {
        super(context, "appachhi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSessionTable = "CREATE TABLE IF NOT EXISTS sessions (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  startTime INT," +
                "  synStatus INT," +
                "  versionCode INT," +
                "  versionName TEXT," +
                "  packageName TEXT," +
                "  syncStatus INT" +
                ")";

        String createAPIEntryTable = "CREATE TABLE IF NOT EXISTS api_calls (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
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
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  appCpuUsage INT," +
                "  deviceCpuUsage INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";
        String createFpsTable = "CREATE TABLE IF NOT EXISTS fps (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  fps INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createFrameDropTable = "CREATE TABLE IF NOT EXISTS frame_drops (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  dropped INT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createGcTable = "CREATE TABLE IF NOT EXISTS gc (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
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
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  fileName TEXT," +
                "  filePath TEXT," +
                "  mimeType TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createScreenshotTable = "CREATE TABLE IF NOT EXISTS screenshots (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  fileName TEXT," +
                "  filePath TEXT," +
                "  mimeType TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createMemoryUsageTabel = "CREATE TABLE IF NOT EXISTS memory_usages (" +
                "  _ID TEXT PRIMARY KEY NOT NULL," +
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
                "  _ID TEXT PRIMARY KEY NOT NULL," +
                "  className TEXT," +
                "  leakTrace TEXT," +
                "  sessionId TEXT," +
                "  executionTime INT," +
                "  sessionTime INT," +
                "  syncStatus INT" +
                ")";

        String createMethodTraceTable = "CREATE TABLE IF NOT EXISTS method_traces (" +
                "_ID TEXT PRIMARY KEY NOT NULL," +
                "trace TEXT," +
                "duration INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";

        String createTransitionTable = "CREATE TABLE IF NOT EXISTS transition_stats (" +
                "_ID TEXT PRIMARY KEY NOT NULL," +
                "name TEXT," +
                "duration INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";
        String createNetworkUsageTable = "CREATE TABLE IF NOT EXISTS network_usages (" +
                "_ID TEXT PRIMARY KEY NOT NULL," +
                "dataReceived INT," +
                "dataSent INT," +
                "sessionId TEXT," +
                "executionTime INT," +
                "sessionTime INT," +
                "syncStatus INT" +
                ")";

        db.execSQL(createSessionTable);
        Log.d("Helper","Session Table Created");
        db.execSQL(createAPIEntryTable);
        Log.d("Helper","API Table Created");
        db.execSQL(createCpuUsageTable);
        Log.d("Helper","CpuUsage Table Created");
        db.execSQL(createFpsTable);
        Log.d("Helper","Fps Table Created");
        db.execSQL(createFrameDropTable);
        Log.d("Helper","FrameDrop Table Created");
        db.execSQL(createGcTable);
        Log.d("Helper","GC Table Created");
        db.execSQL(createLogsTable);
        Log.d("Helper","Logs Table Created");
        db.execSQL(createScreenshotTable);
        Log.d("Helper","Screenshot Table Created");
        db.execSQL(createMemoryLeakTable);
        Log.d("Helper","MemoryLeak Table Created");
        db.execSQL(createMemoryUsageTabel);
        Log.d("Helper","Memory Usage Table Created");
        db.execSQL(createMethodTraceTable);
        Log.d("Helper","Method Trace Table Created");
        db.execSQL(createTransitionTable);
        Log.d("Helper","Transition Table Created");
        db.execSQL(createNetworkUsageTable);
        Log.d("Helper","Network Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
