package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */


public class PositionChangeUpdateTest extends AbstractUpdateTest {

    @Before
    public void setUp(){
        when(mockUserRepository.getUser(creator1)).thenReturn(mockUser);
    }


    @Test
    public void applyUpdate() throws Exception {
        PositionChangeUpdate update = new PositionChangeUpdate(date, creator1, lat, lon, date.getTime());
        update.applyUpdate(mockRepos);
        verify(mockUserRepository).getUser(creator1);
        verify(mockUser).setLocation(any(Location.class));
        verify(mockUserRepository).updateUser(mockUser);
    }

}