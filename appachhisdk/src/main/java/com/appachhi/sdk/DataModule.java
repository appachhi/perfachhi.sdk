package com.appachhi.sdk;

import android.support.annotation.NonNull;

/**
 * Interface defining a publisher which can publish data which can observed by {@link DataObserver}
 *
 * @param <T>
 */
public interface DataModule<T> {
    void start();

    void stop();

    void addObserver(@NonNull DataObserver<T> dataObserver);

    void removeObserver(@NonNull DataObserver<T> dataObserver);

    void notifyObserver();
}
