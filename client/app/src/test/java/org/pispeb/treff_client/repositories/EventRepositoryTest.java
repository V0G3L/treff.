package org.pispeb.treff_client.repositories;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.repositories.EventRepository;

import static org.mockito.Mockito.verify;

/**
 * TODO doc
 */

public class EventRepositoryTest extends RepositoryTest {

    private EventRepository testEventRepository;

    @Before
    public void prepare() {
        testEventRepository = new EventRepository(mockEventDao, mockEncoder,
                mockHandler);
    }

    @Test
    public void addEventTest() {
        testEventRepository.addEvent(mockEvent);

        verify(mockEventDao).save(mockEvent);
    }

    @Test
    public void updateEventTest() {
        testEventRepository.updateEvent(mockEvent);

        verify(mockEventDao).update(mockEvent);
    }

    @Test
    public void deleteEventTest() {
        testEventRepository.deleteEvent(mockEvent.getId());

        verify(mockEventDao).delete(new Event(mockEvent.getId(), null, null,
         null, null, 0, 0));
    }

    @Ignore
    @Test
    public void requestAddEventTest() {
        // TODO verify and SharedPreferences
        testEventRepository.requestAddEvent(mockEvent.getGroupId(), mockEvent
                .getName(), mockEvent.getStart(), mockEvent.getEnd(),
                mockEvent.getLocation());
    }
}
