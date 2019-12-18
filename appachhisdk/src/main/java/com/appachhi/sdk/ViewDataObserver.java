package com.appachhi.sdk;

import android.view.View;
import android.view.ViewGroup;




public interface ViewDataObserver<T> extends DataObserver<T> {

    View createView( ViewGroup root);
}
