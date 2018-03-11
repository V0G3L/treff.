package org.pispeb.treffpunkt.client.view.home.eventList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

/**
 * Stores and manages the necessary data for the EventListFragment
 */

public class EventListViewModel extends ViewModel implements EventListAdapter.EventClickedListener {

    private SingleLiveEvent<State> state;

    private LiveData<PagedList<Event>> events;
    private final EventRepository eventRepository;

    public EventListViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.events = eventRepository.getEvents();
        state = new SingleLiveEvent<>();
    }

    public LiveData<PagedList<Event>> getEvents() {
        return events;
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    @Override
    public void onItemClicked(int position, Event event) {
        state.setValue(new State(ViewCall.DISPLAY_EVENT_DETAILS, event.getId()));
    }
}
