package com.appachhi.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.appachhi.sdk.instrument.trace.Trace;
import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;
public class LeakingActivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaking);
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeakingActivity.this.startAsyncWork();
            }
        });
    }

    @Trace(name = "Leaking Async Work")
    private void startAsyncWork() {
        // This runnable is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the thread finishes (e.g. rotation),
        // the activity getInstance will leak.
        Runnable work = new Runnable() {
            @Override public void run() {
                // Do some slow work in background
                SystemClock.sleep(20000);
            }
        };
        new Thread(work).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);
    }
}
