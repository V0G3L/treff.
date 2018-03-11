package org.pispeb.treffpunkt.client.view.group.eventList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.view.home.eventList.EventListAdapter;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

public class GroupEventListViewModel extends ViewModel implements
        EventListAdapter.EventClickedListener {

    private SingleLiveEvent<State> state;

    private LiveData<PagedList<Event>> events;
    private final EventRepository eventRepository;

    private int groupId;

    public GroupEventListViewModel(
            EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.events = eventRepository.getEvents();

        this.state = new SingleLiveEvent<>();
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
        this.events = eventRepository.getEventsFromGroup(groupId);
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
