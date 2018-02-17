package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;

import java.util.List;
import java.util.Set;

/**
 * {@link Event} repository serving as a bridge between ViewModels and local data/network requests
 */
public class EventRepository {

    private EventDao eventDao;
    private RequestEncoder encoder;
    private final Handler backgroundHandler;

    /**
     * Creates a new {@link EventRepository}
     * @param eventDao the eventDao
     * @param encoder the requestEncoder
     * @param backgroundHandler the backgroundHandler
     */
    public EventRepository(EventDao eventDao, RequestEncoder encoder, Handler backgroundHandler) {
        this.eventDao = eventDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    /**
     * Returns a {@link PagedList} of all {@link Event}s
     * @return the {@link PagedList} of {@link Event}s
     */
    public LiveData<PagedList<Event>> getEvents() {
        return new LivePagedListBuilder<>(eventDao.getAllEvents(), 30)
                .build();
    }

    /**
     * Returns a {@link PagedList} of {@link Event}s of a {@link Set} of {@link org.pispeb.treff_client.data.entities.UserGroup}s
     * @param {@link Set} of {@link org.pispeb.treff_client.data.entities.UserGroup}s
     * @return {@link PagedList} of {@link Event}s
     */
    public LiveData<PagedList<Event>> getEventsFromGroups(Set<UserGroup> g) {
        return null;//new LivePagedListBuilder<>(eventDao.getEventsFromGroups
        // (g),
//                30).build();
    }

    //TODO: remove or doc
    public LiveData<Event> getEvent(int eventID) {
        return eventDao.getEventByID(eventID);
    }

    /**
     * Adds a new {@link Event}
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        backgroundHandler.post(() -> {
            eventDao.save(event);
        });
    }

    public void update(Event newEvent) {
        eventDao.update(newEvent);
    }

    public void delete(int eventId) {
        // TODO constructor with only id
//        eventDao.delete(eventId);
    }
}
