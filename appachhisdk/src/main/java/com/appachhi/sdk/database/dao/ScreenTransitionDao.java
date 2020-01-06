package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.TransitionStatEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.TransitionStatEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.TransitionStatEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.TransitionStatEntry.COLUMN_SYNC_STATUS;

public class ScreenTransitionDao {
    private final SQLiteDatabase sqlDB;

    public ScreenTransitionDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertScreenTranData(TransitionStatEntity transitionStatEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.TransitionStatEntry.TABLE_NAME,
                null,
                Contract.TransitionStatEntry.toContentValues(transitionStatEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<TransitionStatEntity> allUnSyncedScreenTransitionForSession(List<String> sessionIds) {
        Cursor cursor = sqlDB.query(
                Contract.TransitionStatEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"
        );

        return mapCursorToTransitionStat(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.TransitionStatEntry.TABLE_NAME,
                Contract.TransitionStatEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.TransitionStatEntry._ID, join(ids)),
                null
        );
    }

    public List<TransitionStatEntity> allScreenTransition() {
        Cursor cursor = sqlDB.query(
                Contract.TransitionStatEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToTransitionStat(cursor);
    }

    private List<TransitionStatEntity> mapCursorToTransitionStat(Cursor cursor) {
        List<TransitionStatEntity> transitionStatEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            transitionStatEntities.add(Contract.TransitionStatEntry.fromCursor(cursor));
        }
        cursor.close();
        return transitionStatEntities;
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