package com.appachhi.sdk;

import android.support.annotation.LayoutRes;

public abstract class BaseViewDataObserver<T> implements ViewDataObserver<T> {
    @LayoutRes
    private int layoutResId;

    public BaseViewDataObserver(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    protected int getLayoutResId() {
        return layoutResId;
    }
}
