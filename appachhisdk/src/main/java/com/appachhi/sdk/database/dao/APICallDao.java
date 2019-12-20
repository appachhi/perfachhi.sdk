package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.APICallEntity;
import com.appachhi.sdk.database.entity.Contract;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.APICallEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.APICallEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.APICallEntry.COLUMN_SYNC_STATUS;

public class APICallDao {

    private final SQLiteDatabase sqlDB;

    public APICallDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertApiCall(APICallEntity apiCallEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.APICallEntry.TABLE_NAME,
                null,
                Contract.APICallEntry.toContentValues(apiCallEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<APICallEntity> allUnSyncedApiCallEntityForSession(List<String> sessionIds) {
        Cursor cursor = sqlDB.query(
                Contract.APICallEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionIds)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"
        );
        return mapCursorToAPIEntity(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.APICallEntry.TABLE_NAME,
                Contract.APICallEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.APICallEntry._ID, join(ids)),
                null
        );
    }

    public List<APICallEntity> allApiCalls() {
        Cursor cursor = sqlDB.query(
                Contract.APICallEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToAPIEntity(cursor);
    }

    private List<APICallEntity> mapCursorToAPIEntity(Cursor cursor) {
        List<APICallEntity> apiCallEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            apiCallEntities.add(Contract.APICallEntry.fromCursor(cursor));
        }
        cursor.close();
        return apiCallEntities;
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
