package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.MemoryEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.MemoryEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.MemoryEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.MemoryEntry.COLUMN_SYNC_STATUS;

public class MemoryDao {
    private final SQLiteDatabase sqlDB;

    public MemoryDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertMemoryUsage(MemoryEntity memoryUsage){
        return sqlDB.insertWithOnConflict(
                Contract.MemoryEntry.TABLE_NAME,
                null,
                Contract.MemoryEntry.toContentValues(memoryUsage),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<MemoryEntity> allUnSyncedMemoryEntityForSession(List<String> sessionIds){
        Cursor cursor = sqlDB.query(
                Contract.MemoryEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"
        );

        return mapCursorToMemoryUsage(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids){
      sqlDB.update(
                Contract.MemoryEntry.TABLE_NAME,
                Contract.MemoryEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.CpuUsageEntry._ID, join(ids)),
                null
        );
    }

    List<MemoryEntity> allMemoryUsage(){
        Cursor cursor = sqlDB.query(
                Contract.CpuUsageEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToMemoryUsage(cursor);
    }

    private List<MemoryEntity> mapCursorToMemoryUsage(Cursor cursor) {
        List<MemoryEntity> memoryEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            memoryEntities.add(Contract.MemoryEntry.fromCursor(cursor));
        }
        cursor.close();
        return memoryEntities;
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
}
