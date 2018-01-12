package org.pispeb.treff_client.view.home.eventList;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.repositories.EventRepository;

import java.util.List;

/**
 * Stores and manages the necessary data for the EventListFragment
 */

public class EventListViewModel extends ViewModel implements EventListAdapter.EventClickedListener {

    private MutableLiveData<List<Event>> events;
    private final EventRepository eventRepository;

    public EventListViewModel(EventRepository eventRepository) {
        this.events = new MutableLiveData<>();
        this.eventRepository = eventRepository;
    }

    public MutableLiveData<List<Event>> getEvents() {
        return events;
    }

    @Override
    public void onItemClicked(int position, Event event) {
        //TODO display Event details
    }


}
