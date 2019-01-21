package com.appachhi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appachhi.sdk.transition.ScreenTransitionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.goto_second).setOnClickListener(this::onClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this);
    }

    private void onClick(View v) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
