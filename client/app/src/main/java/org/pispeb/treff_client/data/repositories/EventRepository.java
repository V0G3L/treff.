package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;
import org.pispeb.treff_client.view.util.TreffPunkt;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.Set;

/**
 * {@link Event} repository serving as a bridge between ViewModels and local
 * data/network requests
 */
public class EventRepository {

    private EventDao eventDao;
    private RequestEncoder encoder;
    private final Handler backgroundHandler;

    /**
     * Creates a new {@link EventRepository}
     *
     * @param eventDao          the eventDao
     * @param encoder           the requestEncoder
     * @param backgroundHandler the backgroundHandler
     */
    public EventRepository(EventDao eventDao, RequestEncoder encoder,
                           Handler backgroundHandler) {
        this.eventDao = eventDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    /**
     * Returns a {@link PagedList} of all {@link Event}s
     *
     * @return the {@link PagedList} of {@link Event}s
     */
    public LiveData<PagedList<Event>> getEvents() {
        return new LivePagedListBuilder<>(eventDao.getAllEvents(), 30)
                .build();
    }

    /**
     * Returns a {@link PagedList} of {@link Event}s of a {@link Set} of
     * {@link org.pispeb.treff_client.data.entities.UserGroup}s
     *
     * @param idSet{@link Set} of ids of
     *          {@link org.pispeb.treff_client.data.entities.UserGroup}s
     * @return {@link PagedList} of {@link Event}s
     */
    public LiveData<PagedList<Event>> getEventsFromGroups(Set<Integer> idSet) {
        return new LivePagedListBuilder<>(eventDao.getEventsFromGroups(idSet),
                30).build();
    }

    /**
     * Returns a {@link PagedList} of {@link Event}s of a
     * {@link org.pispeb.treff_client.data.entities.UserGroup}
     *
     * @param g id of {@link org.pispeb.treff_client.data.entities.UserGroup}
     * @return {@link PagedList} of {@link Event}s
     */
    public LiveData<PagedList<Event>> getEventsFromGroup(int groupId) {
        return new LivePagedListBuilder<>(
                eventDao.getEventsFromGroup(groupId), 30)
                .build();
    }

    /**
     * Get a LiveData object of a single event, given
     * @param eventID the events id
     * @return LiveData object of that event
     */
    public LiveData<Event> getEvent(int eventId) {
        return eventDao.getEventByID(eventId);
    }

    /**
     * Adds a new {@link Event}
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        backgroundHandler.post(() -> {
            eventDao.save(event);
        });
    }

    /**
     * update an existing event with new information
     * @param newEvent event to replace the old version of itself with
     */
    public void updateEvent(Event newEvent) {
        eventDao.update(newEvent);
    }

    /**
     * delete an event from the local Database
     * @param eventId id of the event to be deleted
     */
    public void deleteEvent(int eventId) {
        eventDao.delete(new Event(eventId, null, null, null, null, 0, 0));
    }

    /**
     * request an id from server for a newly created event
     * @param groupId group to which the event is added
     * @param name name of the event
     * @param start time at which the event starts
     * @param end time at which the event ends
     * @param l Location of the event
     */
    public void requestAddEvent(int groupId, String name, Date start,
                                Date end, Location l) {
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int creator = pref.getInt(ctx.getString(R.string.key_userId), -1);

        Position p = new Position(l.getLatitude(), l.getLongitude());
        encoder.createEvent(groupId, name, creator, start, end, p);
    }

    /**
     * sync with server
     * TODO
     */
    public void requestRefresh() {
        // update events
    }
}
