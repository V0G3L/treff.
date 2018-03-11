package org.pispeb.treffpunkt.client.data.entities.converter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

/**
 * Created by matth on 11.03.2018.
 */

public class DateConverterTest {

    private long timestampZero = 0;
    private long timestampPresentation = 1521158400;
    private long timestampNull;

    private Date dateZero = new Date(0);
    private Date datePresentation = new Date(1521158400);
    private Date dateNull = null;

    @Test
    public void testToDate() {
        assertEquals(dateZero, DateConverter.toDate(timestampZero));
        assertEquals(datePresentation, DateConverter.toDate(timestampPresentation));
        assertEquals(dateZero, DateConverter.toDate(timestampNull));
    }

    @Test
    public void testToTimestamp() {
        long testTimestamp1 = DateConverter.toTimestamp(dateZero);
        assertEquals(timestampZero, testTimestamp1);

        long testTimestamp2 = DateConverter.toTimestamp(datePresentation);
        assertEquals(timestampPresentation, testTimestamp2);

        boolean exception = false;
        try {
            long testTimestamp3 = DateConverter.toTimestamp(dateNull);
        } catch (NullPointerException e) {
            exception = true;
        }
        assertTrue(exception);
    }
}
