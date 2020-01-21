package com.appachhi.sdk.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract.SessionEntry;
import com.appachhi.sdk.database.entity.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionDao {
    private SQLiteDatabase sqlDB;

    public SessionDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertSession(Session session) {
        return sqlDB.insertWithOnConflict(
                SessionEntry.TABLE_NAME,
                null,
                SessionEntry.toContentValues(session),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<Session> allSessions() {
        Cursor cursor = sqlDB.query(
                SessionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return mapCursorToSession(cursor);
    }

    private List<Session> mapCursorToSession(Cursor cursor) {
        List<Session> sessions = new ArrayList<>();
        while (cursor.moveToNext()) {
            sessions.add(SessionEntry.fromCursor(cursor));
        }
        cursor.close();
        return sessions;
    }

    public List<Session> allUnSyncedSessions() {
        Cursor cursor = sqlDB.query(
                SessionEntry.TABLE_NAME,
                null,
                "syncStatus = 0",
                null,
                null,
                null,
                null
        );
        return mapCursorToSession(cursor);
    }

    public List<String> allSyncedSessionIds() {
        Cursor cursor = sqlDB.query(
                SessionEntry.TABLE_NAME,
                new String[]{SessionEntry._ID},
                "syncStatus = 1",
                null,
                null,
                null,
                null
        );
        List<String> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            ids.add(SessionEntry.fromCursorToId(cursor));
        }
        cursor.close();
        return ids;
    }

    public void updateSuccessSyncStatus(List<String> ids) {
        int rowAffected = sqlDB.update(
                SessionEntry.TABLE_NAME,
                SessionEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", SessionEntry._ID, join(ids)),
                null
        );
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
