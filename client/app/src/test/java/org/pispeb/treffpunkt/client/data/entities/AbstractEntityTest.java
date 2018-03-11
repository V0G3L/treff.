package org.pispeb.treffpunkt.client.data.entities;

import android.location.Location;

import org.junit.Before;
import org.mockito.Mock;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */

public abstract class AbstractEntityTest {

    protected int[] ids = {69, 88, 10, 3245, 1424, 948, 4234};
    protected String[] names = {"Hans", "Peter", "Jörg", "Axel"};
    protected String[] messageStrings = {"Hallo", "Tchüss", "Uff..."};
    protected String[] groupNames = {"PSE", "Biatches", "Elite"};
    protected Date date1 = new Date(0);
    protected Date date2 = new Date(1521158400);

    protected ChatMessage message1 =
            new ChatMessage(ids[0], messageStrings[0], ids[1], names[0], date1);

    @Mock
    Location mockLocation = mock(Location.class);

    protected Event event1 =
            new Event(ids[0], names[0], date1, date2, mockLocation, ids[1], ids[2]);

    protected GroupMembership groupMembership1 = new GroupMembership(ids[0], ids[1]);

    protected User user1 = new User(ids[0], names[0], false,
            false, false, false, mockLocation);

    protected UserGroup userGroup1 = new UserGroup(ids[0], groupNames[0]);

    @Before
    public void setMocks() {
        when(mockLocation.getLatitude()).thenReturn(55.55);
        when(mockLocation.getLongitude()).thenReturn(90.00);
        when(mockLocation.getTime()).thenReturn((long)1521158400);
    }


}
