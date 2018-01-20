package org.pispeb.treff_client.view.group.eventList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.Occasion;
import org.pispeb.treff_client.data.entities.Poll;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.PollRepository;

import java.util.List;

/**
 * Created by Lukas on 1/7/2018.
 */

public class GroupEventListViewModel extends ViewModel {

    private LiveData<PagedList<Event>> events;
    private LiveData<PagedList<Poll>> polls;
    private final EventRepository eventRepository;
    private final PollRepository pollRepository;

    public GroupEventListViewModel(
            EventRepository eventRepository,
            PollRepository pollRepository) {
        this.eventRepository = eventRepository;
        this.pollRepository = pollRepository;
        this.events = eventRepository.getEvents();
        this.polls = pollRepository.getPolls();
    }

    public LiveData<PagedList<Event>> getEvents() {
        return events;
    }

    public LiveData<PagedList<Poll>> getPolls() {
        return polls;
    }
}
