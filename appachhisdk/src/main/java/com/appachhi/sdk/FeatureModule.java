package com.appachhi.sdk;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * Abstract Feature Module Class which combines a {@link DataModule} and a {@link DataObserver}
 * <p>
 * Developer just need to subclass this class and connect the compatible {@link DataModule}
 * and {@link DataObserver}
 *
 * @param <T>
 */
public abstract class FeatureModule<T> implements DataModule<T>, ViewDataObserver<T> {
    private DataModule<T> publisher;
    private DataObserver<T> subscriber;
    private ViewDataObserver<T> viewDataObserver;
    private boolean isOverlayEnabled = false;

    public FeatureModule(DataModule<T> publisher, DataObserver<T> subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }

    public FeatureModule(DataModule<T> publisher, ViewDataObserver<T> viewDataObserver, DataObserver<T> subscriber) {
        this(publisher, subscriber);
        this.viewDataObserver = viewDataObserver;
    }

    @Override
    public void start() {
        publisher.addObserver(subscriber);
        if (viewDataObserver != null) {
            publisher.addObserver(viewDataObserver);
        }
        publisher.start();
    }

    @Override
    public void stop() {
        publisher.removeObserver(subscriber);
        if (viewDataObserver != null) {
            publisher.removeObserver(viewDataObserver);
        }
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

    public boolean isOverlayEnabled() {
        return isOverlayEnabled;
    }

    public void setOverlayEnabled(boolean overlayEnabled) {
        isOverlayEnabled = overlayEnabled;
    }

    @Override
    public void notifyObservers() {
        publisher.notifyObservers();
    }

    @Override
    public void onDataAvailable(@NonNull T data) {
        subscriber.onDataAvailable(data);
    }

    @Override
    public View createView(@NonNull ViewGroup root) {
        if (viewDataObserver != null && isOverlayEnabled()) {
            return viewDataObserver.createView(root);
        }
        return null;
    }

    public DataModule<T> getPublisher() {
        return publisher;
    }
}
