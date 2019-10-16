package com.appachhi.sdk.monitor.logs;

import java.io.File;
import java.util.Date;

public class LogsInfo {
    private File logFilePath;
    private Date startedTime;

    public LogsInfo(File logFilePath, Date startedTime) {
        this.logFilePath = logFilePath;
        this.startedTime = startedTime;
    }

    public File getLogFilePath() {
        return logFilePath;
    }

    public Date getStartedTime() {
        return startedTime;
    }
}
