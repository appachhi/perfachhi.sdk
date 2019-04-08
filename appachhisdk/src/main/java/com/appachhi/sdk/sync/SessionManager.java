package com.appachhi.sdk.sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.Session;

import java.util.concurrent.ExecutorService;

public class SessionManager {
    @Nullable
    private Session currentSession;
    @NonNull
    private SessionDao sessionDao;
    @NonNull
    private ExecutorService databaseExecutor;

    public SessionManager(@NonNull SessionDao sessionDao, @NonNull ExecutorService databaseExecutor) {
        this.sessionDao = sessionDao;
        this.databaseExecutor = databaseExecutor;
    }

    private Runnable createNewSession = new Runnable() {
        @Override
        public void run() {
            Session session = Session.create();
            long result = sessionDao.insertSession(session);
            if (result != -1) {
                SessionManager.this.currentSession = session;
            }

        }
    };

    @Nullable
    public Session getCurrentSession() {
        return currentSession;
    }

    public void newSession() {
        databaseExecutor.submit(createNewSession);
    }
}