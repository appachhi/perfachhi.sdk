package com.appachhi.sdk.instrument.transition;

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

public class ScreenTransitionViewDataObserver extends BaseViewDataObserver<TransitionStat> {
    private TextView dataTextView;

    ScreenTransitionViewDataObserver() {
        super(R.layout.simple_layout);
    }

    @Nullable
    @Override
    public View createView(@NonNull ViewGroup root) {
        View view = LayoutInflater.from(root.getContext()).inflate(getLayoutResId(), null);
        dataTextView = view.findViewById(R.id.data);
        dataTextView.setTextColor(Color.WHITE);
        TextView typeTextView = view.findViewById(R.id.type);
        if (typeTextView != null) {
            typeTextView.setText("ST");
            typeTextView.setTextColor(Color.BLACK);
            typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.color_screen_transition_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull TransitionStat data) {
        if (dataTextView != null) {
            dataTextView.setText(String.format(Locale.ENGLISH,"%d ms",
                    data.transitionDuration()));
        }
    }
}
