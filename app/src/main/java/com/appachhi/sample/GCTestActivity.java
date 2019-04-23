package com.appachhi.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appachhi.sdk.instrument.trace.Trace;
import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

public class GCTestActivity extends AppCompatActivity {
    @Trace(name = "GCTEST")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc_test);
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime.getRuntime().gc();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);
    }
}
