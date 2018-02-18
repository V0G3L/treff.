package org.pispeb.treff_client.view.home.eventList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.repositories.EventRepository;

/**
 * Stores and manages the necessary data for the EventListFragment
 */

public class EventListViewModel extends ViewModel implements EventListAdapter.EventClickedListener {

    private LiveData<PagedList<Event>> events;
    private final EventRepository eventRepository;

    public EventListViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.events = eventRepository.getEvents();
    }

    public LiveData<PagedList<Event>> getEvents() {
        return events;
    }


    @Override
    public void onItemClicked(int position, Event event) {
        //TODO display Event details
    }


}
