package com.appachhi.sdk.database.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.CpuUsageEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.CpuUsageEntry;
import static com.appachhi.sdk.database.entity.Contract.CpuUsageEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.CpuUsageEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.CpuUsageEntry.COLUMN_SYNC_STATUS;

public class CpuUsageDao {
    private final SQLiteDatabase sqlDB;

    public CpuUsageDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertCpuUsage(CpuUsageEntity cpuUsage) {
        return sqlDB.insertWithOnConflict(
                CpuUsageEntry.TABLE_NAME,
                null,
                CpuUsageEntry.toContentValues(cpuUsage),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<CpuUsageEntity> allUnSyncedCpuEntityForSession(List<String> sessionId) {
        Cursor cursor = sqlDB.query(
                CpuUsageEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "100"
        );

        return mapCursorToCpuUsage(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids){
        sqlDB.update(
                Contract.CpuUsageEntry.TABLE_NAME,
                Contract.CpuUsageEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", CpuUsageEntry._ID, join(ids)),
                null
        );
    }


    public List<CpuUsageEntity> allCpuUsage(){
        Cursor cursor = sqlDB.query(
                CpuUsageEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToCpuUsage(cursor);
    }

    private List<CpuUsageEntity> mapCursorToCpuUsage(Cursor cursor) {
        List<CpuUsageEntity> cpuUsageEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            cpuUsageEntities.add(CpuUsageEntry.fromCursor(cursor));
        }
        cursor.close();
        return cpuUsageEntities;
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
