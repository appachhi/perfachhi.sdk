package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.StartupEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.StartupEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.StartupEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.StartupEntry.COLUMN_SYNC_STATUS;

public class StartupDao {

    private final SQLiteDatabase sqlDB;

    public StartupDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    private static String join(List<String> input) {
        if (input == null || input.size() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append("'");
            sb.append(input.get(i));
            sb.append("'");
            // if not the last item
            if (i != input.size() - 1) {
                sb.append(",");
            }

        }
        return sb.toString();

    }

    //Create Entity
    public long insertStartupTimeinfo(StartupEntity startupEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.StartupEntry.TABLE_NAME,
                null,
                Contract.StartupEntry.toContentValues(startupEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<StartupEntity> allUnSyncedStartupEntityForSession(List<String> sessionId) {
        Cursor cursor = sqlDB.query(
                Contract.StartupEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"

        );
        return mapCursorToStartupTime(cursor);
    }

    public void updateSucessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.StartupEntry.TABLE_NAME,
                Contract.StartupEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.StartupEntry._ID, join(ids)),
                null
        );
    }

    public List<StartupEntity> allStartupTime() {
        Cursor cursor = sqlDB.query(
                Contract.StartupEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToStartupTime(cursor);
    }

    private List<StartupEntity> mapCursorToStartupTime(Cursor cursor) {
        List<StartupEntity> startupEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            startupEntities.add(Contract.StartupEntry.fromCursor(cursor));
        }
        cursor.close();
        return startupEntities;
    }
}