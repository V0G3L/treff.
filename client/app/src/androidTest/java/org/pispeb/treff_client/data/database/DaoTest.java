package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

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
        //TODO fix
        ld.observeForever(observer);
        return ld.getValue();
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
