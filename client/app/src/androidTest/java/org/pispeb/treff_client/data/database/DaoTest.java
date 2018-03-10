package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Super class for testing DAOs
 */
public abstract class DaoTest {

    private TreffDatabase testDb;

    protected TreffDatabase getTestDb() {
        return testDb;
    }

    /**
     * Returns the value of a LiveData object
     * @param ld the LiveData
     * @param observer an observer, can be a mock
     * @param <T> the LiveData's datatype
     * @return the LiveData's value
     */
    protected <T> T getValueFromLiveData(LiveData<T> ld, Observer observer) {
        ld.observeForever(observer);
        return ld.getValue();
    }

    protected static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        testDb = Room.inMemoryDatabaseBuilder(context, TreffDatabase.class)
                .build();
    }

    @Before
    public abstract void setDao();

    @After
    public void closeDb() throws IOException {
        getTestDb().close();
    }

}
