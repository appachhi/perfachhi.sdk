package com.appachhi.sdk;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ViewDataObserver<T> extends DataObserver<T> {
    @Nullable
    View createView(@NonNull ViewGroup root);
}
