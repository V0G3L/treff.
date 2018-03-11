package org.pispeb.treffpunkt.client.data.database;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Super class for testing DAOs
 */
public abstract class DaoTest {


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    private TreffDatabase testDb;

    protected TreffDatabase getTestDb() {
        return testDb;
    }

    protected static <T> T getValueFromLiveData(LiveData <T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T> () {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        testDb = Room.inMemoryDatabaseBuilder(context, TreffDatabase.class)
                .allowMainThreadQueries().build();
    }

    @Before
    public abstract void setDao();

    @After
    public void closeDb() throws IOException {
        getTestDb().close();
    }

}
