package com.appachhi.sdk;



/**
 * Interface defining a publisher which can publish data which can observed by {@link DataObserver}
 *
 * @param <T>
 */
public interface DataModule<T> {
    void start();

    void stop();

    void addObserver( DataObserver<T> dataObserver);

    void removeObserver( DataObserver<T> dataObserver);

    void notifyObservers();
}
