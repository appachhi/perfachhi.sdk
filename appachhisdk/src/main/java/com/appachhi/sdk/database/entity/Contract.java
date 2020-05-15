package com.appachhi.sdk.database.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public class Contract {
    public static class SessionEntry implements BaseColumns {
        public final static String TABLE_NAME = "sessions";
        final static String COLUMN_START_TIME = "startTime";
        public final static String COLUMN_SYNC_STATUS = "syncStatus";
        final static String COLUMN_VERSION_CODE = "versionCode";
        final static String COLUMN_VERSION_NAME = "versionName";
        final static String COLUMN_PACKAGE_NAME = "packageName";


        public static ContentValues toContentValues(Session session) {
            ContentValues values = new ContentValues();
            values.put(_ID, session.getId());
            values.put(COLUMN_START_TIME, session.getStartTime());
            values.put(COLUMN_SYNC_STATUS, session.getSyncStatus());
            values.put(COLUMN_VERSION_CODE, session.getVersionCode());
            values.put(COLUMN_VERSION_NAME, session.getVersionName());
            values.put(COLUMN_PACKAGE_NAME, session.getPackageName());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static Session fromCursor(Cursor cursor) {
            Session session = new Session();
            session.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            session.setStartTime(cursor.getLong(cursor.getColumnIndex(COLUMN_START_TIME)));
            session.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            session.setVersionCode(cursor.getLong(cursor.getColumnIndex(COLUMN_VERSION_CODE)));
            session.setVersionName(cursor.getString(cursor.getColumnIndex(COLUMN_VERSION_NAME)));
            session.setPackageName(cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
            return session;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class CpuUsageEntry implements BaseColumns {
        public final static String TABLE_NAME = "cpu_usages";
        static final String COLUMN_APP_CPU_USAGE = "appCpuUsage";
        static final String COLUMN_DEVICE_CPU_USAGE = "deviceCpuUsage";

        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(CpuUsageEntity cpuUsageEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, cpuUsageEntity.getId());
            values.put(COLUMN_APP_CPU_USAGE, cpuUsageEntity.getAppCpuUsage());
            values.put(COLUMN_DEVICE_CPU_USAGE, cpuUsageEntity.getDeviceCpuUsage());

            values.put(COLUMN_SESSION_ID, cpuUsageEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, cpuUsageEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, cpuUsageEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, cpuUsageEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static CpuUsageEntity fromCursor(Cursor cursor) {
            CpuUsageEntity cpuUsageEntity = new CpuUsageEntity();
            cpuUsageEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            cpuUsageEntity.setAppCpuUsage(cursor.getDouble(cursor.getColumnIndex(COLUMN_APP_CPU_USAGE)));
            cpuUsageEntity.setDeviceCpuUsage(cursor.getDouble(cursor.getColumnIndex(COLUMN_DEVICE_CPU_USAGE)));

            cpuUsageEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            cpuUsageEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            cpuUsageEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            cpuUsageEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return cpuUsageEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class MemoryEntry implements BaseColumns {
        public final static String TABLE_NAME = "memory_usages";
        static final String COLUMN_JAVA_HEAP = "javaHeap";
        static final String COLUMN_NATIVE_HEAP = "nativeHeap";
        static final String COLUMN_SUMMARY_CODE = "summaryCode";
        static final String COLUMN_SUMMARY_STACK = "stack";
        static final String COLUMN_SUMMARY_GRAPHICS = "graphics";
        static final String COLUMN_SUMMARY_PRIVATE_OTHERS = "privateOther";
        static final String COLUMN_SUMMARY_SYSTEM = "summarySystem";
        static final String COLUMN_SUMMARY_TOTAL_SWAP = "totalSwap";
        static final String COLUMN_THRESHOLD = "threshold";
        static final String COLUMN_TOTAL_PSS = "totalPSS";
        static final String COLUMN_TOTAL_PRIVATE_DIRTY = "totalPrivateDirty";
        static final String COLUMN_TOTAL_SHARED_DIRTY = "totalSharedDirty";
        static final String COLUMN_SYSTEM_RESOURCE = "systemResourceMemory";
        static final String COLUMN_SWAP_RESOURCE = "swapMemory";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        public static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";


        public static ContentValues toContentValues(MemoryEntity memoryEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, memoryEntity.getId());
            values.put(COLUMN_JAVA_HEAP, memoryEntity.getJavaHeap());
            values.put(COLUMN_NATIVE_HEAP, memoryEntity.getNativeHeap());
            values.put(COLUMN_SUMMARY_CODE, memoryEntity.getCode());
            values.put(COLUMN_SUMMARY_STACK, memoryEntity.getStack());
            values.put(COLUMN_SUMMARY_GRAPHICS, memoryEntity.getGraphics());
            values.put(COLUMN_SUMMARY_PRIVATE_OTHERS, memoryEntity.getPrivateOther());
            values.put(COLUMN_SUMMARY_SYSTEM, memoryEntity.getSystem());
            values.put(COLUMN_SUMMARY_TOTAL_SWAP, memoryEntity.getTotalSwap());
            values.put(COLUMN_THRESHOLD, memoryEntity.getThreshold());
            values.put(COLUMN_TOTAL_PSS, memoryEntity.getTotalPSS());
            values.put(COLUMN_TOTAL_PRIVATE_DIRTY, memoryEntity.getTotalPrivateDirty());
            values.put(COLUMN_TOTAL_SHARED_DIRTY, memoryEntity.getTotalSharedDirty());
            values.put(COLUMN_SYSTEM_RESOURCE, memoryEntity.getSystemResourceMemory());
            values.put(COLUMN_SWAP_RESOURCE, memoryEntity.getSwapMemory());


            values.put(COLUMN_SESSION_ID, memoryEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, memoryEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, memoryEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, memoryEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static MemoryEntity fromCursor(Cursor cursor) {
            MemoryEntity memoryEntity = new MemoryEntity();
            memoryEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            memoryEntity.setJavaHeap(cursor.getInt(cursor.getColumnIndex(COLUMN_JAVA_HEAP)));
            memoryEntity.setNativeHeap(cursor.getInt(cursor.getColumnIndex(COLUMN_NATIVE_HEAP)));
            memoryEntity.setCode(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_CODE)));
            memoryEntity.setStack(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_STACK)));
            memoryEntity.setGraphics(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_GRAPHICS)));
            memoryEntity.setPrivateOther(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_PRIVATE_OTHERS)));
            memoryEntity.setSystem(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_SYSTEM)));
            memoryEntity.setTotalSwap(cursor.getInt(cursor.getColumnIndex(COLUMN_SUMMARY_TOTAL_SWAP)));
            memoryEntity.setThreshold(cursor.getInt(cursor.getColumnIndex(COLUMN_THRESHOLD)));
            memoryEntity.setTotalPSS(cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PSS)));
            memoryEntity.setTotalPrivateDirty(cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PRIVATE_DIRTY)));
            memoryEntity.setTotalSharedDirty(cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_SHARED_DIRTY)));
            memoryEntity.setSystemResourceMemory(cursor.getInt(cursor.getColumnIndex(COLUMN_SYSTEM_RESOURCE)));
            memoryEntity.setSwapMemory(cursor.getInt(cursor.getColumnIndex(COLUMN_SWAP_RESOURCE)));

            memoryEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            memoryEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            memoryEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            memoryEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return memoryEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class FPSEntry implements BaseColumns {
        public final static String TABLE_NAME = "fps";
        static final String COLUMN_FPS = "fps";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(FpsEntity fpsEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, fpsEntity.getId());
            values.put(COLUMN_FPS, fpsEntity.getFps());

            values.put(COLUMN_SESSION_ID, fpsEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, fpsEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, fpsEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, fpsEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static FpsEntity fromCursor(Cursor cursor) {
            FpsEntity fpsEntity = new FpsEntity();
            fpsEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            fpsEntity.setFps(cursor.getInt(cursor.getColumnIndex(COLUMN_FPS)));

            fpsEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            fpsEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            fpsEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            fpsEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return fpsEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }


    public static class GCEntry implements BaseColumns {
        public final static String TABLE_NAME = "gc";
        static final String COLUMN_GC_NAME = "gcName";
        static final String COLUMN_OBJECT_FREED = "objectFreed";
        static final String COLUMN_OBJECT_FREED_SIZE = "objectFreedSize";
        static final String COLUMN_ALLOC_SPACE_OBJECT_FREED = "allocSpaceObjectFreed";
        static final String COLUMN_ALLOC_SPACE_OBJECT_FREED_SIZE = "allocSpaceObjectFreedSize";
        static final String COLUMN_LARGE_OBJECT_FREED_PERCENTAGE = "largeObjectFreedPercentage";
        static final String COLUMN_LARGE_OBJECT_FREED_SIZE = "largeObjectFreedSize";
        static final String COLUMN_LARGE_OBJECT_TOTAL_SIZE = "largeObjectTotalSize";
        static final String COLUMN_GC_PAUSE_TIME = "gcPauseTime";
        static final String COLUMN_GC_RUN_TIME = "gcRunTime";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(GCEntity gcEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, gcEntity.getId());
            values.put(COLUMN_GC_NAME, gcEntity.getGcName());
            values.put(COLUMN_OBJECT_FREED, gcEntity.getObjectFreed());
            values.put(COLUMN_OBJECT_FREED_SIZE, gcEntity.getObjectFreedSize());
            values.put(COLUMN_ALLOC_SPACE_OBJECT_FREED, gcEntity.getAllocSpaceObjectFreed());
            values.put(COLUMN_ALLOC_SPACE_OBJECT_FREED_SIZE, gcEntity.getAllocSpaceObjectFreedSize());
            values.put(COLUMN_LARGE_OBJECT_FREED_PERCENTAGE, gcEntity.getLargeObjectFreedPercentage());
            values.put(COLUMN_LARGE_OBJECT_FREED_SIZE, gcEntity.getLargeObjectFreedSize());
            values.put(COLUMN_LARGE_OBJECT_TOTAL_SIZE, gcEntity.getLargeObjectTotalSize());
            values.put(COLUMN_GC_PAUSE_TIME, gcEntity.getGcPauseTime());
            values.put(COLUMN_GC_RUN_TIME, gcEntity.getGcRunTime());

            values.put(COLUMN_SESSION_ID, gcEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, gcEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, gcEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, gcEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static GCEntity fromCursor(Cursor cursor) {
            GCEntity gcEntity = new GCEntity();
            gcEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            gcEntity.setGcName(cursor.getString(cursor.getColumnIndex(COLUMN_GC_NAME)));
            gcEntity.setObjectFreed(cursor.getString(cursor.getColumnIndex(COLUMN_OBJECT_FREED)));
            gcEntity.setObjectFreedSize(cursor.getString(cursor.getColumnIndex(COLUMN_OBJECT_FREED_SIZE)));
            gcEntity.setAllocSpaceObjectFreed(cursor.getString(cursor.getColumnIndex(COLUMN_ALLOC_SPACE_OBJECT_FREED)));
            gcEntity.setAllocSpaceObjectFreedSize(cursor.getString(cursor.getColumnIndex(COLUMN_ALLOC_SPACE_OBJECT_FREED_SIZE)));
            gcEntity.setLargeObjectFreedPercentage(cursor.getString(cursor.getColumnIndex(COLUMN_LARGE_OBJECT_FREED_PERCENTAGE)));
            gcEntity.setLargeObjectFreedSize(cursor.getString(cursor.getColumnIndex(COLUMN_LARGE_OBJECT_FREED_SIZE)));
            gcEntity.setLargeObjectTotalSize(cursor.getString(cursor.getColumnIndex(COLUMN_LARGE_OBJECT_TOTAL_SIZE)));
            gcEntity.setGcPauseTime(cursor.getString(cursor.getColumnIndex(COLUMN_GC_PAUSE_TIME)));
            gcEntity.setGcRunTime(cursor.getString(cursor.getColumnIndex(COLUMN_GC_RUN_TIME)));

            gcEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            gcEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            gcEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            gcEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return gcEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class LogsEntry implements BaseColumns {
        public final static String TABLE_NAME = "logs";
        static final String COLUMN_FILE_NAME = "fileName";
        static final String COLUMN_FILE_PATH = "filePath";
        static final String COLUMN_MIME_TYPE = "mimeType";

        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(LogsEntity logsEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, logsEntity.getId());
            values.put(COLUMN_FILE_NAME, logsEntity.getFileName());
            values.put(COLUMN_FILE_PATH, logsEntity.getFilePath());
            values.put(COLUMN_MIME_TYPE, logsEntity.getMimeType());


            values.put(COLUMN_SESSION_ID, logsEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, logsEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, logsEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, logsEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static LogsEntity fromCursor(Cursor cursor) {
            LogsEntity logsEntity = new LogsEntity(

                    cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH))
            );
            logsEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            return logsEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }


    public static class ScreenshotEntry implements BaseColumns {
        public final static String TABLE_NAME = "screenshots";

        static final String COLUMN_FILE_NAME = "fileName";
        static final String COLUMN_FILE_PATH = "filePath";
        static final String COLUMN_MIME_TYPE = "mimeType";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";


        public static ContentValues toContentValues(ScreenshotEntity screenshotEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, screenshotEntity.getId());
            values.put(COLUMN_FILE_NAME, screenshotEntity.getFileName());
            values.put(COLUMN_FILE_PATH, screenshotEntity.getFilePath());
            values.put(COLUMN_MIME_TYPE, screenshotEntity.getMimeType());


            values.put(COLUMN_SESSION_ID, screenshotEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, screenshotEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, screenshotEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, screenshotEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static ScreenshotEntity fromCursor(Cursor cursor) {
            ScreenshotEntity screenshotEntity = new ScreenshotEntity(
                    cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_MIME_TYPE))
            );
            screenshotEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            return screenshotEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class NetworkUsageEntry implements BaseColumns {
        public final static String TABLE_NAME = "network_usages";
        static final String COLUMN_DATA_SENT = "dataSent";
        static final String COLUMN_DATA_RECEIVED = "dataReceived";

        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";


        public static ContentValues toContentValues(NetworkUsageEntity networkUsageEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, networkUsageEntity.getId());
            values.put(COLUMN_DATA_SENT, networkUsageEntity.getDataSent());
            values.put(COLUMN_DATA_RECEIVED, networkUsageEntity.getDataReceived());

            values.put(COLUMN_SESSION_ID, networkUsageEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, networkUsageEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, networkUsageEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, networkUsageEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static NetworkUsageEntity fromCursor(Cursor cursor) {
            NetworkUsageEntity networkUsageEntity = new NetworkUsageEntity();
            networkUsageEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            networkUsageEntity.setDataReceived(cursor.getLong(cursor.getColumnIndex(COLUMN_DATA_RECEIVED)));
            networkUsageEntity.setDataSent(cursor.getLong(cursor.getColumnIndex(COLUMN_DATA_SENT)));

            networkUsageEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            networkUsageEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            networkUsageEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            networkUsageEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return networkUsageEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }


    public static class MemoryLeakEntry implements BaseColumns {
        public final static String TABLE_NAME = "memory_leaks";

        static final String COLUMN_CLASS_NAME = "className";
        static final String COLUMN_LEAK_TRACE = "leakTrace";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(MemoryLeakEntity memoryLeakEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, memoryLeakEntity.getId());
            values.put(COLUMN_CLASS_NAME, memoryLeakEntity.getClassName());
            values.put(COLUMN_LEAK_TRACE, memoryLeakEntity.getLeakTrace());

            values.put(COLUMN_SESSION_ID, memoryLeakEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, memoryLeakEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, memoryLeakEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, memoryLeakEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static MemoryLeakEntity fromCursor(Cursor cursor) {
            MemoryLeakEntity memoryLeakEntity = new MemoryLeakEntity();
            memoryLeakEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            memoryLeakEntity.setClassName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_NAME)));
            memoryLeakEntity.setLeakTrace(cursor.getString(cursor.getColumnIndex(COLUMN_LEAK_TRACE)));

            memoryLeakEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            memoryLeakEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            memoryLeakEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            memoryLeakEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return memoryLeakEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class APICallEntry implements BaseColumns {
        public final static String TABLE_NAME = "api_calls";
        static final String COLUMN_URL = "url";
        static final String COLUMN_METHOD_TYPE = "methodType";
        static final String COLUMN_CONTENT_TYPE = "contentType";
        static final String COLUMN_REQUEST_CONTENT_LENGTH = "requestContentLength";
        static final String COLUMN_RESPONSE_CODE = "responseCode";
        static final String COLUMN_DURATION = "duration";
        static final String COLUMN_THREAD_NAME = "threadName";

        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";


        public static ContentValues toContentValues(APICallEntity apiCallEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, apiCallEntity.getId());
            values.put(COLUMN_URL, apiCallEntity.getUrl());
            values.put(COLUMN_METHOD_TYPE, apiCallEntity.getMethodType());
            values.put(COLUMN_CONTENT_TYPE, apiCallEntity.getContentType());
            values.put(COLUMN_REQUEST_CONTENT_LENGTH, apiCallEntity.getRequestContentLength());
            values.put(COLUMN_RESPONSE_CODE, apiCallEntity.getResponseCode());
            values.put(COLUMN_DURATION, apiCallEntity.getDuration());
            values.put(COLUMN_THREAD_NAME, apiCallEntity.getThreadName());

            values.put(COLUMN_SESSION_ID, apiCallEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, apiCallEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, apiCallEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, apiCallEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static APICallEntity fromCursor(Cursor cursor) {
            APICallEntity apiCallEntity = new APICallEntity();
            apiCallEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            apiCallEntity.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
            apiCallEntity.setMethodType(cursor.getString(cursor.getColumnIndex(COLUMN_METHOD_TYPE)));
            apiCallEntity.setContentType(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT_TYPE)));
            apiCallEntity.setRequestContentLength(cursor.getLong(cursor.getColumnIndex(COLUMN_REQUEST_CONTENT_LENGTH)));
            apiCallEntity.setResponseCode(cursor.getInt(cursor.getColumnIndex(COLUMN_RESPONSE_CODE)));
            apiCallEntity.setDuration(cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION)));
            apiCallEntity.setThreadName(cursor.getString(cursor.getColumnIndex(COLUMN_THREAD_NAME)));

            apiCallEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            apiCallEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            apiCallEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            apiCallEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return apiCallEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class TransitionStatEntry implements BaseColumns {
        public final static String TABLE_NAME = "transition_stats";

        static final String COLUMN_NAME = "name";
        static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(TransitionStatEntity transitionStatEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, transitionStatEntity.getId());
            values.put(COLUMN_NAME, transitionStatEntity.getName());
            values.put(COLUMN_DURATION, transitionStatEntity.getDuration());

            values.put(COLUMN_SESSION_ID, transitionStatEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, transitionStatEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, transitionStatEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, transitionStatEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static TransitionStatEntity fromCursor(Cursor cursor) {
            TransitionStatEntity transitionStatEntity = new TransitionStatEntity();
            transitionStatEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            transitionStatEntity.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            transitionStatEntity.setDuration(cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION)));

            transitionStatEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            transitionStatEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            transitionStatEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            transitionStatEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return transitionStatEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class StartupEntry implements BaseColumns {
        public final static String TABLE_NAME = "startup_time";

        public static final String COLUMN_COLD_START_VALUE = "coldStartValue";
        public static final String COLUMN_WARM_START_VALUE = "warmStartValue";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        public static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(StartupEntity startupEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, startupEntity.getId());
            values.put(COLUMN_COLD_START_VALUE, startupEntity.getColdStartValue());
            values.put(COLUMN_WARM_START_VALUE, startupEntity.getWarmStartValue());

            values.put(COLUMN_SESSION_ID, startupEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, startupEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, startupEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, startupEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static StartupEntity fromCursor(Cursor cursor) {
            StartupEntity startupEntity = new StartupEntity();
            startupEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            startupEntity.setColdStartValue(cursor.getLong(cursor.getColumnIndex(COLUMN_COLD_START_VALUE)));
            startupEntity.setWarmStartValue(cursor.getLong(cursor.getColumnIndex(COLUMN_WARM_START_VALUE)));

            startupEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            startupEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            startupEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            startupEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return startupEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }

    public static class MethodTraceEntry implements BaseColumns {
        public final static String TABLE_NAME = "method_traces";

        public static final String COLUMN_TRACE_NAME = "traceName";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        public static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(MethodTraceEntity methodTraceEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, methodTraceEntity.getId());
            values.put(COLUMN_TRACE_NAME, methodTraceEntity.getTraceName());
            values.put(COLUMN_DURATION, methodTraceEntity.getDuration());

            values.put(COLUMN_SESSION_ID, methodTraceEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, methodTraceEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, methodTraceEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, methodTraceEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static MethodTraceEntity fromCursor(Cursor cursor) {
            MethodTraceEntity methodTraceEntity = new MethodTraceEntity();
            methodTraceEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            methodTraceEntity.setTraceName(cursor.getString(cursor.getColumnIndex(COLUMN_TRACE_NAME)));
            methodTraceEntity.setDuration(cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION)));

            methodTraceEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            methodTraceEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            methodTraceEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            methodTraceEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return methodTraceEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }


    public static class FrameDropEntry implements BaseColumns {
        public final static String TABLE_NAME = "frame_drops";
        public static final String COLUMN_DROPPED = "dropped";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        public static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "syncStatus";

        public static ContentValues toContentValues(FrameDropEntity frameDropEntity) {
            ContentValues values = new ContentValues();
            values.put(_ID, frameDropEntity.getId());
            values.put(COLUMN_DROPPED, frameDropEntity.getDropped());

            values.put(COLUMN_SESSION_ID, frameDropEntity.getSessionId());
            values.put(COLUMN_EXECUTION_TIME, frameDropEntity.getExecutionTime());
            values.put(COLUMN_SESSION_TIME, frameDropEntity.getSessionTime());
            values.put(COLUMN_SYNC_STATUS, frameDropEntity.getSyncStatus());
            return values;
        }

        public static ContentValues updateSyncStatusValue() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SYNC_STATUS, 1);
            return contentValues;
        }

        public static FrameDropEntity fromCursor(Cursor cursor) {
            FrameDropEntity frameDropEntity = new FrameDropEntity();
            frameDropEntity.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            frameDropEntity.setDropped(cursor.getInt(cursor.getColumnIndex(COLUMN_DROPPED)));

            frameDropEntity.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));
            frameDropEntity.setExecutionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_EXECUTION_TIME)));
            frameDropEntity.setSessionTime(cursor.getLong(cursor.getColumnIndex(COLUMN_SESSION_TIME)));
            frameDropEntity.setSyncStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_SYNC_STATUS)));
            return frameDropEntity;
        }

        public static String fromCursorToId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(_ID));
        }
    }
}