package com.appachhi.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appachhi.sdk.transition.ScreenTransitionManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this,"SecondScreen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this,"SecondScreen");
    }
}
