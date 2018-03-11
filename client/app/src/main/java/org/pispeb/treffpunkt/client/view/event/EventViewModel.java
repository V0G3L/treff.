package org.pispeb.treffpunkt.client.view.event;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;


/**
 * Created by Lukas on 3/11/2018.
 */

public class EventViewModel extends ViewModel {

    private LiveData<Event> event;
    private UserGroup group;
    private final EventRepository eventRepository;

    public EventViewModel(
            EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void deleteEvent(Event event) {
        eventRepository.requestDeleteEvent(event.getGroupId(),
                event.getId());
    }

    public void setEventById(int eventId) {
        event = eventRepository.getEventLiveData(eventId);
    }

    public LiveData<Event> getEvent() {
        return event;
    }
}
