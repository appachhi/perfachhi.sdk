package com.appachhi.sdk;

import android.support.annotation.NonNull;

import com.appachhi.sdk.DataModule;
import com.appachhi.sdk.DataObserver;

/**
 * Abstract Feature Module Class which combines a {@link DataModule} and a {@link DataObserver}
 * <p>
 * Developer just need to subclass this class and connect the compatible {@link DataModule}
 * and {@link DataObserver}
 *
 * @param <T>
 */
public abstract class FeatureModule<T> implements DataModule<T>, DataObserver<T> {
    private DataModule<T> publisher;
    private DataObserver<T> subscriber;

    public FeatureModule(DataModule<T> publisher, DataObserver<T> subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }

    @Override
    public void start() {
        publisher.addObserver(subscriber);
        publisher.start();
    }

    @Override
    public void stop() {
        publisher.removeObserver(subscriber);
        publisher.stop();
    }

    @Override
    public void addObserver(@NonNull DataObserver<T> dataObserver) {
        publisher.addObserver(dataObserver);
    }

    @Override
    public void removeObserver(@NonNull DataObserver<T> dataObserver) {
        publisher.removeObserver(dataObserver);
    }

    @Override
    public void notifyObserver() {
        publisher.notifyObserver();
    }

    @Override
    public void onDataAvailable(@NonNull T data) {
        subscriber.onDataAvailable(data);
    }
}
