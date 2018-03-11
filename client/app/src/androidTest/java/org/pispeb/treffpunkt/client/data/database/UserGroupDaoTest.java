package org.pispeb.treffpunkt.client.data.database;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing functionality of UserGroup database operations
 */
@RunWith(AndroidJUnit4.class)
public class UserGroupDaoTest extends DaoTest {

    private UserGroupDao userGroupDao;

    @Override
    @Before
    public void setDao() {
        userGroupDao = getTestDb().getUserGroupDao();
    }

    @Test
    public void insertUserGroupTest() {
        //TODO test
    }
}
