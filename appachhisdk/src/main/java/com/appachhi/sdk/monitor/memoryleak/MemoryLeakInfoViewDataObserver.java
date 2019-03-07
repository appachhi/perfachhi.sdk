package com.appachhi.sdk.monitor.memoryleak;

import android.app.Application;
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

import java.util.List;

public class MemoryLeakInfoViewDataObserver extends BaseViewDataObserver<List<MemoryLeakInfo>> {

    private TextView dataTextView;
    private Application application;

    public MemoryLeakInfoViewDataObserver(Application application) {
        super(R.layout.simple_layout);
        this.application = application;
    }

    @Nullable
    @Override
    public View createView(@NonNull ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        dataTextView = view.findViewById(R.id.data);
        dataTextView.setTextColor(Color.WHITE);
        TextView typeTextView = view.findViewById(R.id.type);
        if (typeTextView != null) {
            typeTextView.setText("Leaks");
            typeTextView.setTextColor(Color.BLACK);
            typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.color_memory_leaks_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull List<MemoryLeakInfo> data) {
        if (dataTextView != null && data.size() > 0) {
            dataTextView.setText(application.getString(R.string.memory_leak_view_message, data.size()));
        }
    }
}
