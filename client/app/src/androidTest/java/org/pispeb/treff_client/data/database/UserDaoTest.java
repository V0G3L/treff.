package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.Observer;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.data.entities.User;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Testing functionality of User database operations
 */
@RunWith(AndroidJUnit4.class)
public class UserDaoTest extends DaoTest {

    @Mock
    private Observer<List<User>> mockUserListObserver;
    private Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
    private UserDao userDao;

    private User testUserHans = new User(1337,
            "Hans Wurst",
            true,
            false,
            false,
            false,
            mockLocation);
    private User testUserPeter = new User(9001,
            "Peter Pan",
            false,
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

        assertTrue(userDao.getUserByName(testUserHans.getUsername()).equals(testUserHans));
    }

    @Test
    public void getUserByIdTest() {
        //depends on insert
        userDao.save(testUserHans);

        User daoUser = getValueFromLiveData(
                userDao.getUserByIdLiveData(testUserHans.getUserId()), mockUserListObserver);
        assertTrue(daoUser.equals(testUserHans));
    }

    @Test
    public void deleteUserTest() {
        //depends on insert
        userDao.save(testUserHans);

        LinkedList<User> deleteList = new LinkedList<>();
        deleteList.add(testUserHans);
        userDao.deleteAll(deleteList);

        assertEquals(userDao.getAllAsList().size(), 0);
    }

    @Test
    public void friendTest() {
        //depends on insert

        //no friends
        List<User> friendList = getValueFromLiveData(userDao.getFriendsAsList(), mockUserListObserver);
        assertEquals(friendList.size(), 0);

        //add friend
        userDao.save(testUserHans);
        friendList = getValueFromLiveData(userDao.getFriendsAsList(), mockUserListObserver);
        assertEquals(friendList.size(), 1);

        //remove friend
        userDao.setIsFriend(testUserHans.getUserId(), false);
        friendList = getValueFromLiveData(userDao.getFriendsAsList(), mockUserListObserver);
        assertEquals(friendList.size(), 0);
    }

    @Test
    public void friendRequestTest() {
        //depends on insert

        //add non friend non requesting
        userDao.save(testUserPeter);

        //TODO find out how to test datafactory stuff
        userDao.getFriendsAndPending();


        userDao.setIsPending(testUserPeter.getUserId(), true);


        userDao.save(testUserHans);


    }

}
