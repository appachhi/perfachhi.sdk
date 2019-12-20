package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.GCEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.GCEntry;
import static com.appachhi.sdk.database.entity.Contract.GCEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.GCEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.GCEntry.COLUMN_SYNC_STATUS;

public class GCDao {
    private final SQLiteDatabase sqlDB;

    public GCDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insetGcRunInfo(GCEntity gcRun) {
        return sqlDB.insertWithOnConflict(
                GCEntry.TABLE_NAME,
                null,
                GCEntry.toContentValues(gcRun),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<GCEntity> allUnSyncedGcEntityForSession(List<String> sessionIds) {
        Cursor cursor = sqlDB.query(
                GCEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"
        );

        return mapCursorToGC(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                GCEntry.TABLE_NAME,
                GCEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", GCEntry._ID, join(ids)),
                null
        );
    }

    List<GCEntity> allGCRun() {
        Cursor cursor = sqlDB.query(
                GCEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToGC(cursor);
    }


    private List<GCEntity> mapCursorToGC(Cursor cursor) {
        List<GCEntity> gcEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            gcEntities.add(GCEntry.fromCursor(cursor));
        }
        cursor.close();
        return gcEntities;
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
