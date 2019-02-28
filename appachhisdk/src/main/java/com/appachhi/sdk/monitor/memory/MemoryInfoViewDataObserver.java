package com.appachhi.sdk.monitor.memory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appachhi.sdk.BaseViewDataObserver;
import com.appachhi.sdk.R;

import java.util.Locale;

public class MemoryInfoViewDataObserver extends BaseViewDataObserver<MemoryInfo> {
    private TextView memoryInfoTextView;

    MemoryInfoViewDataObserver() {
        super(R.layout.simple_text);
    }

    @Nullable
    @Override
    public View createView(@NonNull ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        memoryInfoTextView = (TextView) view;
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull MemoryInfo data) {
        memoryInfoTextView.setText(String.format(Locale.ENGLISH, "App Memory %d", data.getTotalPssMemory()));
    }
}
