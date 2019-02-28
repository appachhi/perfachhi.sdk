package com.appachhi.sdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

public interface ViewDataObserver<T> extends DataObserver<T> {
    @Nullable
    View createView(@NonNull ViewGroup root);
}
