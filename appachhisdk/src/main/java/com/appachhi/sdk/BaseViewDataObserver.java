package com.appachhi.sdk;



public abstract class BaseViewDataObserver<T> implements ViewDataObserver<T> {

    private int layoutResId;

    public BaseViewDataObserver( int layoutResId) {
        this.layoutResId = layoutResId;
    }

    protected int getLayoutResId() {
        return layoutResId;
    }
}
