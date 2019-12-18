package com.appachhi.sdk.database.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.MemoryLeakEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.MemoryLeakEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.MemoryLeakEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.MemoryLeakEntry.COLUMN_SYNC_STATUS;


public class MemoryLeakDao {
    private final SQLiteDatabase sqlDB;

    public MemoryLeakDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertMemoryLeak(MemoryLeakEntity memoryLeakEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.MemoryLeakEntry.TABLE_NAME,
                null,
                Contract.MemoryLeakEntry.toContentValues(memoryLeakEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<MemoryLeakEntity> allUnSyncedMemoryLeakEntityForSession(List<String> sessionIds) {
        Cursor cursor = sqlDB.query(
                Contract.MemoryLeakEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToMemoryLeak(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.MemoryLeakEntry.TABLE_NAME,
                Contract.MemoryLeakEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.MemoryLeakEntry._ID, join(ids)),
                null
        );
    }

    public List<MemoryLeakEntity> allMemoryLeak() {
        Cursor cursor = sqlDB.query(
                Contract.MemoryLeakEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToMemoryLeak(cursor);
    }
    

    private List<MemoryLeakEntity> mapCursorToMemoryLeak(Cursor cursor) {
        List<MemoryLeakEntity> memoryLeakEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            memoryLeakEntities.add(Contract.MemoryLeakEntry.fromCursor(cursor));
        }
        cursor.close();
        return memoryLeakEntities;
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
