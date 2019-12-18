package com.appachhi.sdk.database.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.ScreenshotEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.ScreenshotEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.ScreenshotEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.ScreenshotEntry.COLUMN_SYNC_STATUS;

public class ScreenshotDao {
    private final SQLiteDatabase sqlDB;

    public ScreenshotDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertScreenshot(ScreenshotEntity screenshotEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.LogsEntry.TABLE_NAME,
                null,
                Contract.ScreenshotEntry.toContentValues(screenshotEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<ScreenshotEntity> unSyncedScreenshotEntityForSession(List<String> sessionId, int limit) {
        Cursor cursor = sqlDB.query(
                Contract.ScreenshotEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "" + limit
        );

        return mapCursorToScreenshot(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        sqlDB.update(
                Contract.ScreenshotEntry.TABLE_NAME,
                Contract.ScreenshotEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.ScreenshotEntry._ID, join(ids)),
                null
        );
    }

    public List<ScreenshotEntity> allScreenshots() {
        Cursor cursor = sqlDB.query(
                Contract.ScreenshotEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );
        return mapCursorToScreenshot(cursor);
    }


    private List<ScreenshotEntity> mapCursorToScreenshot(Cursor cursor) {
        List<ScreenshotEntity> screenshotEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            screenshotEntities.add(Contract.ScreenshotEntry.fromCursor(cursor));
        }
        cursor.close();
        return screenshotEntities;
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
