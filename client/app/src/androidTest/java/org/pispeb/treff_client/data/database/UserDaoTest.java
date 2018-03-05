package org.pispeb.treff_client.data.database;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing functionality of UserGroups database operations
 */
public class UserDaoTest extends DaoTest {

    private UserDao userDao;

    @Override
    @Before
    public void setDao() {
        userDao = getTestDb().getUserDao();
    }

    @Test
    public void insertMessageTest() {
        //TODO test
    }
}
