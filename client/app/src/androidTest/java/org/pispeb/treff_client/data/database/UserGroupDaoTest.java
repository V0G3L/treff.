package org.pispeb.treff_client.data.database;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing functionality of UserGroups database operations
 */
public class UserGroupDaoTest extends DaoTest {

    private UserGroupDao userGroupDao;

    @Override
    @Before
    public void setDao() {
        userGroupDao = getTestDb().getUserGroupDao();
    }

    @Test
    public void insertMessageTest() {
        //TODO test
    }
}
