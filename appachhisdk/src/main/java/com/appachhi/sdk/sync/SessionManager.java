package com.appachhi.sdk.sync;

import android.content.Context;

import com.appachhi.sdk.database.dao.SessionDao;
import com.appachhi.sdk.database.entity.Session;

import java.util.concurrent.ExecutorService;

public class SessionManager {

    private Session currentSession;

    private SessionDao sessionDao;

    private ExecutorService databaseExecutor;
    private Context context;

    public SessionManager(Context context,  SessionDao sessionDao,  ExecutorService databaseExecutor) {
        this.context = context;
        this.sessionDao = sessionDao;
        this.databaseExecutor = databaseExecutor;
    }


    private Runnable createNewSession = new Runnable() {
        @Override
        public void run() {
            Session session = Session.create(context);
            long result = sessionDao.insertSession(session);
            if (result != -1) {
                SessionManager.this.currentSession = session;
            }
        }
    };


    public Session getCurrentSession() {
        return currentSession;
    }

    public void newSession() {
        databaseExecutor.submit(createNewSession);
    }
}
