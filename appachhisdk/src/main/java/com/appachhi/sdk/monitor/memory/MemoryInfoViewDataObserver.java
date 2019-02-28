package com.appachhi.sdk.monitor.memory;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appachhi.sdk.BaseViewDataObserver;
import com.appachhi.sdk.R;

import java.util.Locale;

public class MemoryInfoViewDataObserver extends BaseViewDataObserver<MemoryInfo> {
    private TextView dataTextView;
    private TextView typeTextView;

    MemoryInfoViewDataObserver() {
        super(R.layout.simple_layout);
    }

    @Nullable
    @Override
    public View createView(@NonNull ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        dataTextView = view.findViewById(R.id.data);
        dataTextView.setTextColor(Color.WHITE);
        typeTextView = view.findViewById(R.id.type);
        if (typeTextView != null) {
            typeTextView.setText("Mem");
            typeTextView.setTextColor(Color.BLACK);
            typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.memory_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull MemoryInfo data) {
        if (dataTextView != null) {
            dataTextView.setText(String.format(Locale.ENGLISH,
                    "%d MB / %d MB",
                    (data.getTotalPrivateDirty() + data.getTotalSharedDirty()) / 1024,
                    (data.getThreshold() / 1024)));
        }
    }
}
