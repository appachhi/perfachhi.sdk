package com.appachhi.sdk;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.appachhi.sdk.DataModule;

/**
 * Interface defining a Data Subscriber
 *
 * @param <T>
 */
public interface DataObserver<T> {

    /**
     * The implementation classes should override this method in order to perform action once the
     * data is received.All the result will be delivered on the main thread
     *
     * @param data Data Published by {@link DataModule}
     */
    @MainThread
    void onDataAvailable(@NonNull T data);
}
