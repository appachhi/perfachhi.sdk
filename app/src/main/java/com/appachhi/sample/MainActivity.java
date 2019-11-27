package com.appachhi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appachhi.sdk.instrument.trace.Trace;
import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

public class MainActivity extends AppCompatActivity {

    @Trace(name = "MainActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.network_usage_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkTestActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.gc_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GCTestActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.memory_leak_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeakingActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.okhttp_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OkHttpTestActivity.class));
            }
        });
//        findViewById(R.id.dump_data).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, BugaSuraDumpService.class);
//                ContextCompat.startForegroundService(MainActivity.this,intent);
//            }
//        });
    }

    @Trace(name = "MainActvity Resume")
    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);

    }

}