package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class EventDeletionUpdateTest extends AbstractUpdateTest {
    @Test
    public void applyUpdate() throws Exception {
        EventDeletionUpdate update = new EventDeletionUpdate(date, creator1, group1, event1);
        update.applyUpdate(mockRepos);
        verify(mockEventRepository).deleteEvent(event1);
    }

}