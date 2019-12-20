package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.FrameDropEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.FrameDropEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.FrameDropEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.FrameDropEntry.COLUMN_SYNC_STATUS;


public class FrameDropDao {
    private final SQLiteDatabase sqlDB;

    public FrameDropDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertFrameDrop(FrameDropEntity frameDropEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.FrameDropEntry.TABLE_NAME,
                null,
                Contract.FrameDropEntry.toContentValues(frameDropEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<FrameDropEntity> allUnSyncedFpsEntityForSession(List<String> sessionId) {
        Cursor cursor = sqlDB.query(
                Contract.FrameDropEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "100"
        );

        return mapCursorToFrameDrop(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.FrameDropEntry.TABLE_NAME,
                Contract.FrameDropEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.FrameDropEntry._ID, join(ids)),
                null
        );
    }

    public List<FrameDropEntity> allFps() {
        Cursor cursor = sqlDB.query(
                Contract.FrameDropEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToFrameDrop(cursor);
    }


    private List<FrameDropEntity> mapCursorToFrameDrop(Cursor cursor) {
        List<FrameDropEntity> frameDropEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            frameDropEntities.add(Contract.FrameDropEntry.fromCursor(cursor));
        }
        cursor.close();
        return frameDropEntities;
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
