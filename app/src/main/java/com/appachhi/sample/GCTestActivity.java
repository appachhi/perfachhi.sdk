package com.appachhi.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class GCTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc_test);
        findViewById(R.id.action).setOnClickListener(v -> {
            Runtime.getRuntime().gc();
        });
    }
}
