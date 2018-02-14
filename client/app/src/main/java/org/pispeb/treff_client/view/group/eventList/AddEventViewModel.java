package org.pispeb.treff_client.view.group.eventList;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.location.LocationManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.Calendar;
import java.util.Date;

/**
 * ViewModel for creating a new event
 */

public class AddEventViewModel extends ViewModel
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private EventRepository eventRepository;
    private UserGroupRepository userGroupRepository;

    MutableLiveData<Event> event;

    private SingleLiveEvent<State> state;

    int year, month, dayOfMonth;
    int startHour, startMin;
    int endHour, endMin;

    private int lastDialog = NO_DIALOG;
    private final static int NO_DIALOG = 0;
    private final static int START_DIALOG = 1;
    private final static int END_DIALOG = 2;


    public AddEventViewModel(EventRepository eventRepository,
                             UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;

        //automatically set to current time
        Date start = new Date();
        // one hour later
        Date end = new Date(start.getTime() + 1000 * 60 * 60);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        event = new MutableLiveData<>();
        event.postValue(new Event("", start, end, location, 0));

        state = new SingleLiveEvent<>();
    }

    public void setGroup(int groupId) {
//        this.event.getValue().getGroupId = userGroupRepository.getGroup(groupId)
//                .getValue();
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onDateClick() {
        state.setValue(new State(ViewCall.SHOW_DATE_DIALOG, 0));
    }

    public void onStartTimeClick() {
        lastDialog = START_DIALOG;
        state.setValue(new State(ViewCall.SHOW_TIME_DIALOG, 0));
    }

    public void onEndTimeClick() {
        lastDialog = END_DIALOG;
        state.setValue(new State(ViewCall.SHOW_TIME_DIALOG, 0));
    }

    public void onSaveClick(Location location) {
        if (event.getValue().getName().equals("")) return;

        // Add Event to Database
        eventRepository.addEvent(event.getValue());

        state.setValue(new State(ViewCall.SUCCESS, 0));
    }

    public String getName() {
        return event.getValue().getName();
    }

    public String getStartString() {
        return event.getValue().getStart().toString();//.substring(11);
    }

    public String getEndString() {
        return event.getValue().getEnd().toString();//.substring(11);
    }

    public String getDateString() {
        return event.getValue().getStart().toString();//.substring(0, 10);
    }

    public Location getLocation() {
        return event.getValue().getLocation();
    }

    public void setName(String name) {
        event.getValue().setName(name);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month,
                          int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        updateTimes();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (lastDialog) {
            case START_DIALOG:
                startHour = hourOfDay;
                startMin = minute;
                break;
            case END_DIALOG:
                endHour = hourOfDay;
                endMin = minute;
                break;
        }

        updateTimes();
        lastDialog = NO_DIALOG;
    }


    private void updateTimes() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth, startHour, startMin);
        event.getValue().setStart(c.getTime());
        c.set(year, month, dayOfMonth, endHour, endMin);
        event.getValue().setEnd(c.getTime());
    }
}
