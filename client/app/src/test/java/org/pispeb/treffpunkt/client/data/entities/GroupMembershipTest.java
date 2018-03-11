package org.pispeb.treffpunkt.client.data.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */

public class GroupMembershipTest extends AbstractEntityTest {


    protected GroupMembership groupMembership2 = new GroupMembership(ids[0], ids[1]);
    protected GroupMembership groupMembership3 = new GroupMembership(ids[0], ids[1]);
    @Test
    public void getUserId() throws Exception {
        assertEquals(ids[0], groupMembership1.getUserId());
    }

    @Test
    public void setUserId() throws Exception {
        groupMembership2.setUserId(65656);
        assertEquals(65656, groupMembership2.getUserId());
    }

    @Test
    public void getGroupId() throws Exception {
        assertEquals(ids[1], groupMembership1.getGroupId());
    }

    @Test
    public void setGroupId() throws Exception {
        groupMembership2.setGroupId(78787);
        assertEquals(78787, groupMembership2.getGroupId());
    }

    @Test
    public void getSharingUntil() throws Exception {
        assertEquals(date1, groupMembership1.getSharingUntil());
    }

    @Test
    public void setSharingUntil() throws Exception {
        groupMembership2.setSharingUntil(date2);
        assertEquals(date2, groupMembership2.getSharingUntil());
    }

    @Test
    public void isSharing() throws Exception {
        assertTrue(!groupMembership1.isSharing());
    }

    @Test
    public void setSharing() throws Exception {
        groupMembership2.setSharing(true);
        assertTrue(groupMembership2.isSharing());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(groupMembership1.equals(groupMembership1));
        assertTrue(groupMembership1.equals(groupMembership3));
    }


}
