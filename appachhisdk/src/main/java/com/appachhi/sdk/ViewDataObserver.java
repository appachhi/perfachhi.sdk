package com.appachhi.sdk;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public interface ViewDataObserver<T> extends DataObserver<T> {
    View createView(@NonNull ViewGroup root);
}
