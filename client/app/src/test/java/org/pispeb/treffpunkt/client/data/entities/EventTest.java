package org.pispeb.treffpunkt.client.data.entities;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class EventTest extends AbstractEntityTest{


    protected Event event2 =
            new Event(ids[0], names[0], date1, date2, mockLocation, ids[1], ids[2]);

    protected Event event3 =
            new Event(ids[0], names[0], date1, date2, mockLocation, ids[1], ids[2]);

    @Test
    public void getId() throws Exception {
        assertEquals(ids[0], event1.getId());
    }

    @Test
    public void setId() throws Exception {
        event2.setId(1234);
        assertEquals(1234, event2.getId());
    }

    @Test
    public void getEnd() throws Exception {
        assertEquals(date2, event1.getEnd());
    }

    @Test
    public void setEnd() throws Exception {
        event2.setEnd(date2);
        assertEquals(date2, event2.getEnd());
    }

    @Test
    public void getStart() throws Exception {
        assertEquals(date1, event1.getStart());
    }

    @Test
    public void setStart() throws Exception {
        event2.setStart(date2);
        assertEquals(date2, event2.getStart());
    }

    @Test
    public void getCreator() throws Exception {
        assertEquals(ids[1], event1.getCreator());
    }

    @Test
    public void setCreator() throws Exception {
        event2.setCreator(54545);
        assertEquals(54545, event2.getCreator());
    }

    @Test
    public void getStartString() throws Exception {
        assertEquals(date1.toString().substring(0, 16), event1.getStartString());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(mockLocation, event1.getLocation());
    }

    @Test
    public void setLocation() throws Exception {
        Location location = new Location(LocationManager.PASSIVE_PROVIDER);
        event2.setLocation(location);
        assertEquals(location, event2.getLocation());
    }

    @Test
    public void getGroupId() throws Exception {
        assertEquals(ids[2], event1.getGroupId());
    }

    @Test
    public void setGroupId() throws Exception {
        event2.setGroupId(98989);
        assertEquals(98989, event2.getGroupId());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(event1.equals(event1));
        assertTrue(event1.equals(event3));
    }



}