package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.LogsEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_SYNC_STATUS;

public class LogsDao {

    private final SQLiteDatabase sqlDB;

    public LogsDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertLogs(LogsEntity logEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.LogsEntry.TABLE_NAME,
                null,
                Contract.LogsEntry.toContentValues(logEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<LogsEntity> unSyncedLogEntityForSession(List<String> sessionId, int limit) {
        Cursor cursor = sqlDB.query(
                Contract.FPSEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "" + limit
        );

        return mapCursorToLog(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.FPSEntry.TABLE_NAME,
                Contract.FPSEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.FPSEntry._ID, join(ids)),
                null
        );
    }

    public List<LogsEntity> allLogs() {
        Cursor cursor = sqlDB.query(
                Contract.FPSEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToLog(cursor);
    }

    private List<LogsEntity> mapCursorToLog(Cursor cursor) {
        List<LogsEntity> logsEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            logsEntities.add(Contract.LogsEntry.fromCursor(cursor));
        }
        cursor.close();
        return logsEntities;
    }

    private static String join(List<String> input) {
        if (input == null || input.size() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));
            // if not the last item
            if (i != input.size() - 1) {
                sb.append(",");
            }

        }
        return sb.toString();

    }
}
