package org.pispeb.treffpunkt.client.data.entities.converter;

import android.location.Location;
import android.location.LocationManager;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PositionConverter.class})
public class PositionConverterTest {

    private String positionString = "55.55#90.0#1521158400";

    @Mock
    Location mockLocation = mock(Location.class);

    @Before
    public void setUp() {
        try {
            PowerMockito.whenNew(Location.class).withArguments(LocationManager.GPS_PROVIDER).thenReturn(mockLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toLocation() throws Exception {
        PositionConverter.toLocation(positionString);
        verify(mockLocation).setLatitude(55.55);
        verify(mockLocation).setLongitude(90.0);
        verify(mockLocation).setTime((long)1521158400);
    }

    @Test
    public void toDoubles() throws Exception {
        when(mockLocation.getLatitude()).thenReturn(55.55);
        when(mockLocation.getLongitude()).thenReturn(90.00);
        when(mockLocation.getTime()).thenReturn((long)1521158400);
        assertEquals(positionString ,PositionConverter.toDoubles(mockLocation));
    }

}