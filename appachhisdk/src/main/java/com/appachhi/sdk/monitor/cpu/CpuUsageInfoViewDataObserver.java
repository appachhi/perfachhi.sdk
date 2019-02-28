package com.appachhi.sdk.monitor.cpu;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appachhi.sdk.BaseViewDataObserver;
import com.appachhi.sdk.R;

public class CpuUsageInfoViewDataObserver extends BaseViewDataObserver<CpuUsageInfo> {
    private TextView cpuUsageTextView;

    public CpuUsageInfoViewDataObserver() {
        super(R.layout.simple_text);
    }

    @Nullable
    @Override
    public View createView(@NonNull ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        cpuUsageTextView = (TextView) view;
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull CpuUsageInfo data) {
        cpuUsageTextView.setText(String.format("Total : %s\nApp : %s", data.getTotal(), data.getMyPid()));
    }
}
