package org.pispeb.treffpunkt.client.data.database;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing functionality of EventDao database operations
 */
@RunWith(AndroidJUnit4.class)
public class EventDaoTest extends DaoTest {

    private EventDao eventDao;

    @Override
    @Before
    public void setDao() {
        eventDao = getTestDb().getEventDao();
    }

    @Test
    public void insertEventTest() {
        //TODO test
    }
}
