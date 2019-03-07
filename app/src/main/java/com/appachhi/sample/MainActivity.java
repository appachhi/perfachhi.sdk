package com.appachhi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.network_usage_test).setOnClickListener(v -> {
            Intent intent = new Intent(this, NetworkTestActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.gc_test).setOnClickListener(v -> {
            Intent intent = new Intent(this, GCTestActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.memory_leak_test).setOnClickListener(v -> {
            Intent intent = new Intent(this, LeakingActivity.class);
            startActivity(intent);
        });
        try {
            testTracing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);

    }

    public void testTracing() throws InterruptedException {
        Thread.sleep(10);
        testTracing1();
    }

    public void testTracing1() throws InterruptedException {
        Thread.sleep(10);
        testTracing2();
    }

    public void testTracing2() throws InterruptedException {
        Thread.sleep(10);
    }


    private void onClick(View v) {
        startActivity(new Intent(this, NetworkTestActivity.class));
    }
}