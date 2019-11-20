package com.appachhi.sdk.monitor.fps;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.appachhi.sdk.BaseViewDataObserver;
import com.appachhi.sdk.R;

public class FpsViewDataObserver extends BaseViewDataObserver<Double> {
    private TextView dataTextView;
    private TextView typeTextView;

    FpsViewDataObserver() {
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
            typeTextView.setText("FPS");
            typeTextView.setTextColor(Color.BLACK);
            typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.color_fps_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull Double data) {
        if (dataTextView != null) {
            dataTextView.setText(String.valueOf(data.intValue()));
        }
    }
}
