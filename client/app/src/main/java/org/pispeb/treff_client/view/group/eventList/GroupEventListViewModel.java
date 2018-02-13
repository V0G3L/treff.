package org.pispeb.treff_client.view.group.eventList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.view.home.eventList.EventListAdapter;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

public class GroupEventListViewModel extends ViewModel implements
        EventListAdapter.EventClickedListener {

    SingleLiveEvent<State> state;

    private LiveData<PagedList<Event>> events;
    private final EventRepository eventRepository;

    public GroupEventListViewModel(
            EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.events = eventRepository.getEvents();

        this.state = new SingleLiveEvent<>();
    }

    public void onAddClick() {
        state.setValue(new State(ViewCall.ADD_EVENT, 0));
    }

    @Override
    public void onItemClicked(int position, Event event) {

    }

    public LiveData<PagedList<Event>> getEvents() {
        return events;
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
