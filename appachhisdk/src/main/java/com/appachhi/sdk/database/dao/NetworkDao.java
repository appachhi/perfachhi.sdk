package com.appachhi.sdk.database.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appachhi.sdk.database.entity.Contract;
import com.appachhi.sdk.database.entity.NetworkUsageEntity;

import java.util.ArrayList;
import java.util.List;

import static com.appachhi.sdk.database.entity.Contract.NetworkUsageEntry.COLUMN_EXECUTION_TIME;
import static com.appachhi.sdk.database.entity.Contract.NetworkUsageEntry.COLUMN_SESSION_ID;
import static com.appachhi.sdk.database.entity.Contract.NetworkUsageEntry.COLUMN_SYNC_STATUS;

public class NetworkDao {

    private final SQLiteDatabase sqlDB;

    public NetworkDao(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    public long insertNetworkUsage(NetworkUsageEntity networkUsageEntity) {
        return sqlDB.insertWithOnConflict(
                Contract.NetworkUsageEntry.TABLE_NAME,
                null,
                Contract.NetworkUsageEntry.toContentValues(networkUsageEntity),
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<NetworkUsageEntity> allUnSyncedNetworkUsageEntityForSession(List<String> sessionId) {
        Cursor cursor = sqlDB.query(
                Contract.NetworkUsageEntry.TABLE_NAME,
                null,
                String.format("%s = 0 AND %s IN (%s)", COLUMN_SYNC_STATUS, COLUMN_SESSION_ID, join(sessionId)),
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                "200"
        );

        return mapCursorToNetworkUsage(cursor);
    }

    public void updateSuccessSyncStatus(List<String> ids){
        sqlDB.update(
                Contract.NetworkUsageEntry.TABLE_NAME,
                Contract.NetworkUsageEntry.updateSyncStatusValue(),
                String.format("%s IN (%s)", Contract.NetworkUsageEntry._ID, join(ids)),
                null
        );
    }


    List<NetworkUsageEntity> allNetwork(){
        Cursor cursor = sqlDB.query(
                Contract.NetworkUsageEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EXECUTION_TIME,
                null
        );

        return mapCursorToNetworkUsage(cursor);
    }

    private List<NetworkUsageEntity> mapCursorToNetworkUsage(Cursor cursor) {
        List<NetworkUsageEntity> networkUsageEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            networkUsageEntities.add(Contract.NetworkUsageEntry.fromCursor(cursor));
        }
        cursor.close();
        return networkUsageEntities;
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
