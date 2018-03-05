package org.pispeb.treff_client.data.database;


import org.junit.Before;
import org.junit.Test;

/**
 * Testing functionality of UserGroups database operations
 */
public class EventDaoTest extends DaoTest {

    private EventDao eventDao;

    @Override
    @Before
    public void setDao() {
        eventDao = getTestDb().getEventDao();
    }

    @Test
    public void insertMessageTest() {
        //TODO test
    }
}
