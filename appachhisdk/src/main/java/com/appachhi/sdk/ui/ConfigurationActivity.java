package com.appachhi.sdk.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appachhi.sdk.Appachhi;
import com.appachhi.sdk.OverlayService;
import com.appachhi.sdk.R;

public class ConfigurationActivity extends Activity implements View.OnClickListener {
    public static PendingIntent getPendingIntent(Context context) {
        return PendingIntent.getActivity(context,
                12,
                new Intent(context, ConfigurationActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private ToggleButton screenTransitionSwitch;
    private ToggleButton cpuUsageSwitch;
    private ToggleButton memoryUsageSwitch;
    private ToggleButton networkUsage;
    private ToggleButton fpsUsageSwitch;
    private ToggleButton memoryLeakSwitch;
    private ToggleButton screenCaptureSwitch;


    private TextView screenTransitionText;
    private TextView memoryUsageText;
    private TextView cpuUsageText;
    private TextView networkUsageText;
    private TextView fpsText;
    private TextView methodCallsText;
    private TextView memoryLeaksText;
    private TextView ssCaptureText;
    private TextView apiCallsText;





    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_slide_in, 0);
        setContentView(R.layout.activity_config);

      /*  screenTransitionText = findViewById(R.id.screen_trans_metric_state_text);
        fpsText = findViewById(R.id.fps_metric_state_text);
        memoryUsageText = findViewById(R.id.memoryUsage_metric_state_text);
        cpuUsageText = findViewById(R.id.cpuUsage_metric_state_text);
        networkUsageText = findViewById(R.id.networkUsage_metric_state_text);
        methodCallsText = findViewById(R.id.methodCalls_metric_state_text);
        memoryLeaksText = findViewById(R.id.memoryLeak_metric_state_text);
        apiCallsText = findViewById(R.id.apiCalls_metric_state_text);*/

        ssCaptureText = findViewById(R.id.sessionScreen_metric_state_text);


        View emptyArea = findViewById(R.id.emptyArea);
        emptyArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });
        initializeConfigurationSwitch();
        setConfigurationSwitchListener();
    }

    private void setConfigurationSwitchListener() {
/*
        screenTransitionText.setOnClickListener(this);
        fpsText.setOnClickListener(this);
        memoryUsageText.setOnClickListener(this);
        cpuUsageText.setOnClickListener(this);
        networkUsageText.setOnClickListener(this);
        methodCallsText.setOnClickListener(this);
        memoryLeaksText.setOnClickListener(this);*/
        ssCaptureText.setOnClickListener(this);
        //apiCallsText.setOnClickListener(this);

        /*screenTransitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        screenCaptureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appachhi.getInstance().getFeatureConfigManager().setScreenShotEnable(ConfigurationActivity.this, isChecked);
            }
        });*/
    }


    private void initializeConfigurationSwitch() {


     /*   if (Appachhi.getInstance().getFeatureConfigManager().isScreenTransitionOverlayEnabled()) {
            screenTransitionText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            screenTransitionText.setText("ON");
        } else {
            screenTransitionText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            screenTransitionText.setText("OFF");
        }

        if (Appachhi.getInstance().getFeatureConfigManager().isMemoryInfoOverlayEnabled()) {
            memoryUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            memoryUsageText.setText("ON");
        } else {
            memoryUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            memoryUsageText.setText("OFF");
        }

        if (Appachhi.getInstance().getFeatureConfigManager().isCpuUsageOverlayEnabled()) {
            cpuUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            cpuUsageText.setText("ON");
        } else {
            cpuUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            cpuUsageText.setText("OFF");
        }

        if (Appachhi.getInstance().getFeatureConfigManager().isNetworkOverlayEnabled()) {
            networkUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            networkUsageText.setText("ON");
        } else {
            networkUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            networkUsageText.setText("OFF");
        }


        if (Appachhi.getInstance().getFeatureConfigManager().isFpsOverlayEnabled()) {
            fpsText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            fpsText.setText("ON");

        } else {
            fpsText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            fpsText.setText("OFF");

        }


        if (Appachhi.getInstance().getFeatureConfigManager().isMemoryLeakOverlayEnabled()) {
            memoryLeaksText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            memoryLeaksText.setText("ON");
        } else {
            memoryLeaksText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            memoryLeaksText.setText("OFF");
        }
*/

        if (Appachhi.getInstance().getFeatureConfigManager().isEnableScreenShotEnabled()) {
            ssCaptureText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            ssCaptureText.setText("ON");
        } else {
            ssCaptureText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
            ssCaptureText.setText("OFF");
        }



       /* screenTransitionSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isScreenTransitionOverlayEnabled());
        cpuUsageSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isCpuUsageOverlayEnabled());
        memoryUsageSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isMemoryInfoOverlayEnabled());
        networkUsage.setChecked(Appachhi.getInstance().getFeatureConfigManager().isNetworkOverlayEnabled());
        memoryLeakSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isMemoryLeakOverlayEnabled());
        memoryLeakSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isMemoryLeakOverlayEnabled());
        screenCaptureSwitch.setChecked(Appachhi.getInstance().getFeatureConfigManager().isEnableScreenShotEnabled());*/
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Appachhi.getInstance().getFeatureConfigManager().handleMediaProjectionResult(resultCode, data);
    }

    @Override
    public void onClick(View v) {

       /* if (v == screenTransitionText) {
            if (TextUtils.equals(screenTransitionText.getText().toString(), "OFF")) {
                screenTransitionText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                screenTransitionText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setScreenTransitionOverlay(true);
            } else {
                screenTransitionText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                screenTransitionText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setScreenTransitionOverlay(false);

            }

        }


        if (v == memoryUsageText) {
            if (TextUtils.equals(memoryUsageText.getText().toString(), "OFF")) {
                memoryUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                memoryUsageText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setMemoryInfoOverlayEnabled(true);
            } else {
                memoryUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                memoryUsageText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setMemoryInfoOverlayEnabled(false);

            }
        }


        if (v == cpuUsageText) {
            if (TextUtils.equals(cpuUsageText.getText().toString(), "OFF")) {
                cpuUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                cpuUsageText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setCpuUsageOverlayEnabled(true);
            } else {
                cpuUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                cpuUsageText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setCpuUsageOverlayEnabled(false);

            }
        }


        if (v == networkUsageText) {
            if (TextUtils.equals(networkUsageText.getText().toString(), "OFF")) {
                networkUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                networkUsageText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setNetworkOverlayEnabled(true);
            } else {
                networkUsageText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                networkUsageText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setNetworkOverlayEnabled(false);

            }
        }


        if (v == fpsText) {
            if (TextUtils.equals(fpsText.getText().toString(), "OFF")) {
                fpsText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                fpsText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setFpsOverlayEnabled(true);
            } else {
                fpsText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                fpsText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setFpsOverlayEnabled(false);

            }
        }

        if (v == memoryLeaksText) {
            if (TextUtils.equals(memoryLeaksText.getText().toString(), "OFF")) {
                memoryLeaksText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                memoryLeaksText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setMemoryLeakOverlayEnabled(true);
            } else {
                memoryLeaksText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                memoryLeaksText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setMemoryLeakOverlayEnabled(false);

            }
        }

*/
        if (v == ssCaptureText) {
            if (TextUtils.equals(ssCaptureText.getText().toString(), "OFF")) {
                ssCaptureText.setBackground(getResources().getDrawable(R.drawable.button_shape_on));
                ssCaptureText.setText("ON");

                Appachhi.getInstance().getFeatureConfigManager().setScreenShotEnable(this, true);
            } else {
                ssCaptureText.setBackground(getResources().getDrawable(R.drawable.button_shape_off));
                ssCaptureText.setText("OFF");

                Appachhi.getInstance().getFeatureConfigManager().setScreenShotEnable(this, false);

            }
        }



    }
}
