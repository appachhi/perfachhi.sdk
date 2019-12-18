package com.appachhi.sdk.monitor.cpu;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appachhi.sdk.BaseViewDataObserver;
import com.appachhi.sdk.R;

import java.util.Locale;

public class CpuUsageInfoViewDataObserver extends BaseViewDataObserver<CpuUsageInfo> {
    private TextView dataTextView;

    CpuUsageInfoViewDataObserver() {
        super(R.layout.simple_layout);
    }


    @Override
    public View createView( ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        dataTextView = view.findViewById(R.id.data);
        dataTextView.setTextColor(Color.WHITE);
        TextView typeTextView = view.findViewById(R.id.type);
        if (typeTextView != null) {
            typeTextView.setText("CPU");
            typeTextView.setTextColor(Color.BLACK);
          //  typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.color_cpu_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable( CpuUsageInfo data) {
        if (dataTextView != null) {
            dataTextView.setText(String.format(Locale.ENGLISH,
                    "%1.2f%% / %1.2f%%", data.getMyPid(), data.getTotal()));
        }
    }
}
