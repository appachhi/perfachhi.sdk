package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appachhi.sdk.database.entity.BatteryEntity;
import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.StartupEntity;

import java.util.ArrayList;
import java.util.List;


import static com.appachhi.sdk.database.entity.Contract.BatteryEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.BatteryEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.BatteryEntry.COLUMN_SYNC_STATUS;

public class BatteryDataDao {

    private final SQLiteDatabase sqlDB;

    public BatteryDataDao(SQLiteDatabase sqlDB) {
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
    public long insertBatteryDataInfo(BatteryEntity batteryEntity) {
        Log.d("BatteryDataDao", "Entered here");
        return sqlDB.insertWithOnConflict(
                Contract.BatteryEntry.TABLE_NAME,
                null,
                Contract.BatteryEntry.toContentValues(batteryEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<BatteryEntity> allUnSyncedBatteryEntityForSession(List<String> sessionId) {
        Cursor cursor = sqlDB.query(
                Contract.BatteryEntry.TABLE_NAME,
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
                Contract.BatteryEntry.TABLE_NAME,
                Contract.BatteryEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.BatteryEntry._ID, join(ids)),
                null
        );
    }

    public List<BatteryEntity> allStartupTime() {
        Cursor cursor = sqlDB.query(
                Contract.BatteryEntry.TABLE_NAME,
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

    private List<BatteryEntity> mapCursorToStartupTime(Cursor cursor) {
        List<BatteryEntity> batteryEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            batteryEntities.add(Contract.BatteryEntry.fromCursor(cursor));
        }
        cursor.close();
        return batteryEntities;
    }
}