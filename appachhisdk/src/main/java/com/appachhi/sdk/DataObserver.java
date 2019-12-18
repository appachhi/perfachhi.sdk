package com.appachhi.sdk;

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
    void onDataAvailable( T data);
}
