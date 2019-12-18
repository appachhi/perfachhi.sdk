package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.MethodTraceEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.MethodTraceEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.MethodTraceEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.MethodTraceEntry.COLUMN_SYNC_STATUS;

public class MethodTraceDao {
    private final SQLiteDatabase sqlDB;

    public MethodTraceDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long addTrace(MethodTraceEntity methodTraceEntity){
        return sqlDB.insertWithOnConflict(
                Contract.MethodTraceEntry.TABLE_NAME,
                null,
                Contract.MethodTraceEntry.toContentValues(methodTraceEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<MethodTraceEntity> allUnSyncedMethodTraceEntityForSession(List<String> sessionIds){
        Cursor cursor = sqlDB.query(
                Contract.MethodTraceEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToMethodTrace(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids){
        sqlDB.update(
                Contract.MethodTraceEntry.TABLE_NAME,
                Contract.MethodTraceEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.MethodTraceEntry._ID, join(ids)),
                null
        );
    }


    List<MethodTraceEntity> allMethodTrace(){
        Cursor cursor = sqlDB.query(
                Contract.MethodTraceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToMethodTrace(cursor);
    }

    private List<MethodTraceEntity> mapCursorToMethodTrace(Cursor cursor) {
        List<MethodTraceEntity> methodTraceEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            methodTraceEntities.add(Contract.MethodTraceEntry.fromCursor(cursor));
        }
        cursor.close();
        return methodTraceEntities;
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
