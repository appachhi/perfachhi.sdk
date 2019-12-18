package com.appachhi.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.instrument.trace.MethodTrace;
import com.appachhi.sdk.instrument.trace.Trace;
import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

public class GCTestActivity extends AppCompatActivity {
    @Trace(name = "GCTEST")
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        MethodTrace methodTrace = Appachhi.newTrace("GCTEST");

        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc_test);
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime.getRuntime().gc();
            }
        });
        methodTrace.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);
    }
}
