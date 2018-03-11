package org.pispeb.treffpunkt.client.data.entities;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class UserTest extends AbstractEntityTest{

    protected User user2 = new User(ids[0], names[0], false, false, false, false, mockLocation);
    protected User user3 = new User(ids[0], names[0], false, false, false, false, mockLocation);
    @Test
    public void getUserId() throws Exception {
        assertEquals(ids[0], user1.getUserId());
    }

    @Test
    public void setUserId() throws Exception {
        user2.setUserId(12121);
        assertEquals(12121, user2.getUserId());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(names[0], user1.getUsername());
    }

    @Test
    public void setUsername() throws Exception {
        user2.setUsername("Wurst");
        assertEquals("Wurst", user2.getUsername());
    }

    @Test
    public void isFriend() throws Exception {
        assertFalse(user1.isFriend());
    }

    @Test
    public void setFriend() throws Exception {
        user2.setFriend(true);
        assertTrue(user2.isFriend());
    }

    @Test
    public void isBlocked() throws Exception {
        assertFalse(user1.isBlocked());
    }

    @Test
    public void setBlocked() throws Exception {
        user2.setBlocked(true);
        assertTrue(user2.isBlocked());
    }

    @Test
    public void isRequesting() throws Exception {
        assertFalse(user1.isRequesting());
    }

    @Test
    public void setRequesting() throws Exception {
        user2.setRequesting(true);
        assertTrue(user2.isRequesting());
    }

    @Test
    public void isRequestPending() throws Exception {
        assertFalse(user1.isRequestPending());
    }

    @Test
    public void setRequestPending() throws Exception {
        user2.setRequestPending(true);
        assertTrue(user2.isRequestPending());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(mockLocation, user1.getLocation());
    }

    @Test
    public void setLocation() throws Exception {
        Location location = new Location(LocationManager.PASSIVE_PROVIDER);
        user2.setLocation(location);
        assertEquals(location, user2.getLocation());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(user1.equals(user1));
        assertTrue(user1.equals(user3));
    }




}