package org.pispeb.treff_client.data.database;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.pispeb.treff_client.data.entities.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Testing functionality of User database operations
 */
public class UserDaoTest extends DaoTest {

    private static Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
    private UserDao userDao;

    private User testUserHans = new User(1337,
            "Hans Wurst",
            true,
            false,
            false,
            false,
            mockLocation);

    @Override
    @Before
    public void setDao() {
        userDao = getTestDb().getUserDao();
    }

    @Test
    public void insertUserTest() {
        userDao.save(testUserHans);

        //check for number of users
        assertEquals(userDao.getAllAsList().size(), 1);
        //try to get it back and compare them
        assertTrue(userDao.getAllAsList().contains(testUserHans));
    }


    @Test
    public void getUserByNameTest() {
        //depends on insert
        userDao.save(testUserHans);

        assertTrue(userDao.getUserByName("Hans Wurst").equals(testUserHans));
    }

}
