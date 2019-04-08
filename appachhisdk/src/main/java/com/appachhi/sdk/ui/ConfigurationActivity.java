package com.appachhi.sdk.ui;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.OverlayService;
import com.appachhi.sdk.R;

public class ConfigurationActivity extends AppCompatActivity {
    public static PendingIntent getPendingIntent(Context context) {
        return PendingIntent.getActivity(context,
                12,
                new Intent(context, ConfigurationActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private Switch screenTransitionSwitch;
    private Switch cpuUsageSwitch;
    private Switch memoryUsageSwitch;
    private Switch networkUsage;
    private Switch fpsUsageSwitch;
    private Switch memoryLeakSwitch;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_slide_in, 0);
        setContentView(R.layout.activity_configuration);
        screenTransitionSwitch = findViewById(R.id.screenTransitionSwitch);
        cpuUsageSwitch = findViewById(R.id.cpuUsageSwitch);
        memoryUsageSwitch = findViewById(R.id.memoryUsageSwitch);
        networkUsage = findViewById(R.id.networkUsageSwitch);
        fpsUsageSwitch = findViewById(R.id.fpsUsageSwitch);
        memoryLeakSwitch = findViewById(R.id.memoryLeakSwitch);
        View emptyArea = findViewById(R.id.emptyArea);
        emptyArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        initializeConfigurationSwitch();
        setConfigurationSwitchListener();
    }

    private void setConfigurationSwitchListener() {
        screenTransitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setScreenTransitionOverlay(isChecked);
            }
        });
        cpuUsageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setCpuUsageOverlayEnabled(isChecked);
            }
        });
        memoryUsageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setMemoryInfoOverlayEnabled(isChecked);
            }
        });
        networkUsage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setNetworkOverlayEnabled(isChecked);
            }
        });
        fpsUsageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setFpsOverlayEnabled(isChecked);
            }
        });
        memoryLeakSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setMemoryLeakOverlayEnabled(isChecked);
            }
        });
    }

    private void initializeConfigurationSwitch() {
        screenTransitionSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isScreenTransitionOverlayEnabled());
        cpuUsageSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isCpuUsageOverlayEnabled());
        memoryUsageSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isMemoryInfoOverlayEnabled());
        networkUsage.setChecked(Appachhi.getInstance().getFeatureConfigManager().isNetworkOverlayEnabled());
        memoryLeakSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isMemoryLeakOverlayEnabled());
    }

    @Override
    protected void onStart() {
        OverlayService.hideOverlay(getApplicationContext());
        super.onStart();
    }

    @Override
    protected void onStop() {
        OverlayService.showOverlay(getApplicationContext());
        super.onStop();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.left_slide_out, 0);
        super.onPause();
    }

}
