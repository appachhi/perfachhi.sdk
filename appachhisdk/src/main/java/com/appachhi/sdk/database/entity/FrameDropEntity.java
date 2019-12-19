package com.appachhi.sdk.database.entity;

import android.provider.BaseColumns;

public class FrameDropEntity extends BaseEntity {


    public static class Entry implements BaseColumns {
        public final static String TABLE_NAME = "frame_drops";
        public static final String COLUMN_DROPPED = "dropped";
        public static final String COLUMN_SESSION_ID = "sessionId";
        public static final String COLUMN_EXECUTION_TIME = "executionTime";
        public static final String COLUMN_SESSION_TIME = "sessionTime";
        public static final String COLUMN_SYNC_STATUS = "sessionTime";
    }
}
