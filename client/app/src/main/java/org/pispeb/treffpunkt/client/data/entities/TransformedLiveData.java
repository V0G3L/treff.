package org.pispeb.treffpunkt.client.data.entities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;

/**
 * Abstract {@code LiveData} class that allows constructing {@code LiveData}
 * objects that are generated by transforming other {@code LiveData} objects.
 * The transformed {@code LiveData} objects automatically update themselves
 * in a background-thread when the source object changes.
 *
 * @param <Source> The type of data held by the source {@code LiveData}
 * @param <Target> The type of data held by the transformed {@code LiveData}
 */
public abstract class TransformedLiveData<Source, Target>
        extends LiveData<Target> {

    private final LiveData<Source> sourceLiveData;
    private Observer<Source> observer = source
            -> AsyncTask.execute(() -> postValue(transform(source)));

    protected TransformedLiveData(LiveData<Source> sourceLiveData) {
        this.sourceLiveData = sourceLiveData;
    }

    @Override
    protected void onActive() {
        sourceLiveData.observeForever(observer);
    }

    @Override
    protected void onInactive() {
        sourceLiveData.removeObserver(observer);
    }

    /**
     * Transforms the current value of the observed source {@code LiveData}
     * into the target {@code LiveData}.
     * Is called every time the source {@code LiveData} changes.
     *
     * @param source The current value of the observed source {@code LiveData}.
     * @return The transformed {@link LiveData}
     */
    protected abstract Target transform(Source source);
}