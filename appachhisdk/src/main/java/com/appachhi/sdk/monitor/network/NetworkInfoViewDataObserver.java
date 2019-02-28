package com.appachhi.sdk.monitor.network;

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

public class NetworkInfoViewDataObserver extends BaseViewDataObserver<NetworkInfo> {
    private TextView dataTextView;

    NetworkInfoViewDataObserver() {
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
            typeTextView.setText("Net");
            typeTextView.setTextColor(Color.BLACK);
            typeTextView.setBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.color_network_tag));
        }
        return view;
    }

    @Override
    public void onDataAvailable(@NonNull NetworkInfo data) {
        if (dataTextView != null) {
            dataTextView.setText(String.format(Locale.ENGLISH,
                    "Send : %s\nRec : %s", getDataText(data.getByteSend()),
                    getDataText(data.getByteReceived())));
        }
    }

    private String getDataText(long dataTransmitted) {
        if (dataTransmitted < (1024 * 1024)) {
            return String.format(Locale.ENGLISH, "%d KB", dataTransmitted / 1024);
        } else {
            return String.format(Locale.ENGLISH, "%d MB", dataTransmitted / (1024 * 1204));
        }
    }
}
