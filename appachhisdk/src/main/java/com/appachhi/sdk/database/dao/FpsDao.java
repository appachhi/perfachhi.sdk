package com.appachhi.sdk.database.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.FpsEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.FPSEntry.COLUMN_SYNC_STATUS;

public class FpsDao {
    private final SQLiteDatabase sqlDB;

    public FpsDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }
    public long insertFps(FpsEntity fpsEntity){
        return sqlDB.insertWithOnConflict(
                Contract.FPSEntry.TABLE_NAME,
                null,
                Contract.FPSEntry.toContentValues(fpsEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<FpsEntity> allUnSyncedFpsEntityForSession(List<String> sessionId){
        Cursor cursor = sqlDB.query(
                Contract.FPSEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "100"
        );

        return mapCursorToMemoryUsage(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids){
        sqlDB.update(
                Contract.FPSEntry.TABLE_NAME,
                Contract.FPSEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.FPSEntry._ID, join(ids)),
                null
        );
    }

    public List<FpsEntity> allFps(){
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

        return mapCursorToMemoryUsage(cursor);
    }



    private List<FpsEntity> mapCursorToMemoryUsage(Cursor cursor) {
        List<FpsEntity> cpuUsageEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            cpuUsageEntities.add(Contract.FPSEntry.fromCursor(cursor));
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
