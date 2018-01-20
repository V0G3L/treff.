package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;

import java.util.List;


public class EventRepository {

    private EventDao eventDao;
    private RequestEncoder encoder;
    private final Handler backgroundhandler;

    public EventRepository(EventDao eventDao, RequestEncoder encoder, Handler backgroundhandler) {
        this.eventDao = eventDao;
        this.encoder = encoder;
        this.backgroundhandler = backgroundhandler;
    }

    public LiveData<PagedList<Event>> getEvents() {
        return new LivePagedListBuilder<>(eventDao.getAllEvents(), 30)
                .build();
    }

    public LiveData<Event> getEvent(int eventID) {
        return eventDao.getEventByID(eventID);
    }

    public void addEvent(Event event) {
        backgroundhandler.post(() -> {
            eventDao.save(event);
        });
    }
}
