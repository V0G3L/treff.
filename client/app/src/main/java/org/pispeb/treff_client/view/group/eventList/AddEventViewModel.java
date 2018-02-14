package org.pispeb.treff_client.view.group.eventList;

import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.location.LocationManager;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.Position;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.Date;

/**
 * ViewModel for creating a new event
 */

public class AddEventViewModel extends ViewModel {

    private EventRepository eventRepository;
    private UserGroupRepository userGroupRepository;

    private String name;
    private Date start;
    private Date end;
    private Location location;
    private UserGroup group;

    private SingleLiveEvent<State> state;


    public AddEventViewModel(EventRepository eventRepository,
                             UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;

        name = "";
        //automatically set to current time
        start = new Date();
        // one hour later
        end = new Date(start.getTime() + 1000 * 60 * 60);
        location = new Location(LocationManager.GPS_PROVIDER);
        state = new SingleLiveEvent<>();
    }

    public void setGroup(int groupId) {
        this.group = userGroupRepository.getGroup(groupId).getValue();
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onDateClick() {
    }

    public void onStartTimeClick() {
    }

    public void onEndTimeClick() {
    }

    public void onChooseLocationClick() {
    }

    public void onSaveClick(Location location) {
        if (name.equals("")) return;

        // Add Event to Database
        eventRepository.addEvent(
//                new Event(name, new Date(), start,
//                        new Position(location.getLatitude(),
//                                location.getLongitude()),
//                        0));
                new Event(name, new Date(), start,
                        new Position(location.getLatitude(), location.getLongitude()),
                        0));

        state.setValue(new State(ViewCall.SUCCESS, 0));
    }

    public String getName() {
        return name;
    }

    public String getStartString() {
        return start.toString().substring(11);
    }

    public String getEndString() {
        return end.toString().substring(11);
    }

    public String getDateString() {
        return start.toString().substring(0, 10);
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }
}
