package org.pispeb.treffpunkt.client.data.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class UserGroupTest extends AbstractEntityTest {

    protected UserGroup userGroup2 = new UserGroup(ids[0], groupNames[0]);
    protected UserGroup userGroup3 = new UserGroup(ids[0], groupNames[0]);
    @Test
    public void getGroupId() throws Exception {
        assertEquals(ids[0], userGroup1.getGroupId());
    }

    @Test
    public void setGroupId() throws Exception {
        userGroup2.setGroupId(ids[3]);
        assertEquals(ids[3], userGroup2.getGroupId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(groupNames[0], userGroup1.getName());
    }

    @Test
    public void setName() throws Exception {
        userGroup2.setName(groupNames[2]);
        assertEquals(groupNames[2], userGroup2.getName());
    }

    @Test
    public void isSharingLocation() throws Exception {
        assertFalse(userGroup1.isSharingLocation());
    }

    @Test
    public void setSharingLocation() throws Exception {
        userGroup2.setSharingLocation(true);
        assertTrue(userGroup2.isSharingLocation());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(groupNames[0], userGroup1.toString());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(userGroup1.equals(userGroup1));
        assertTrue(userGroup1.equals(userGroup3));
    }


}