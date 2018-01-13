package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class EventRepository {

    private EventDao eventDao;
    private RequestEncoder encoder;
    private final Handler backgroundhandler;

    public EventRepository(EventDao eventDao, RequestEncoder encoder, Handler backgroundhandler) {
        this.eventDao = eventDao;
        this.encoder = encoder;
        this.backgroundhandler = backgroundhandler;
    }

    public LiveData<List<Event>> getEvents() {
        return eventDao.getEvents();
    }
}
