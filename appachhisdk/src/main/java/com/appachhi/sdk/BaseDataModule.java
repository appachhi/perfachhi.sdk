package com.appachhi.sdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of {@link DataModule} to provide easy was to publish data without
 * handling the responsibility of notifying the observer
 *
 * @param <T>
 */
public abstract class BaseDataModule<T> implements DataModule<T> {
    private List<DataObserver<T>> dataObservers = new ArrayList<>();

    /**
     * Attaches the given {@link DataObserver} to the list of all {@link DataObserver}
     *
     * @param dataObserver {@link DataObserver} to be added
     */
    @Override
    public void addObserver(@NonNull DataObserver<T> dataObserver) {
        if (!dataObservers.contains(dataObserver)) {
            dataObservers.add(dataObserver);
        }
    }

    /**
     * Removes the given {@link DataObserver} from the list of all {@link DataObserver}
     *
     * @param dataObserver {@link DataObserver} to be removed
     */
    @Override
    public void removeObserver(@NonNull DataObserver<T> dataObserver) {
        dataObservers.remove(dataObserver);
    }

    /**
     * Notifies all the {@link DataObserver} if there is non null data
     */
    @Override
    public void notifyObservers() {
        T data = getData();
        if (data != null) {
            for (DataObserver<T> dataObserver : dataObservers) {
                dataObserver.onDataAvailable(data);
            }
        }
    }

    /**
     * Return the current data published by a {@link DataModule}
     *
     * @return data which to be published
     */
    @Nullable
    protected abstract T getData();
}
