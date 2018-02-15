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

    Event event;

    private SingleLiveEvent<State> state;

    MutableLiveData<Calendar> start;
    MutableLiveData<Calendar> end;

    private int lastTimeDialog = NO_DIALOG;
    private int lastDateDialog = NO_DIALOG;
    private final static int NO_DIALOG = 0;
    private final static int START_DIALOG = 1;
    private final static int END_DIALOG = 2;


    public AddEventViewModel(EventRepository eventRepository,
                             UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;

        start = new MutableLiveData<>();
        end = new MutableLiveData<>();
        start.setValue(Calendar.getInstance());
        end.setValue(Calendar.getInstance());
        end.getValue().add(Calendar.HOUR, 1);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        event = new Event("", start.getValue().getTime(), end.getValue().getTime(),
                location, 0);

        state = new SingleLiveEvent<>();
    }

    public MutableLiveData<Calendar> getStart() {
        return start;
    }

    public MutableLiveData<Calendar> getEnd() {
        return end;
    }

    public void setGroup(int groupId) {
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onStartDateClick() {
        lastDateDialog = START_DIALOG;
        state.setValue(new State(ViewCall.SHOW_DATE_DIALOG, 0));
    }


    public void onEndDateClick() {
        lastDateDialog = END_DIALOG;
        state.setValue(new State(ViewCall.SHOW_DATE_DIALOG, 0));
    }

    public void onStartTimeClick() {
        lastTimeDialog = START_DIALOG;
        state.setValue(new State(ViewCall.SHOW_TIME_DIALOG, 0));
    }

    public void onEndTimeClick() {
        lastTimeDialog = END_DIALOG;
        state.setValue(new State(ViewCall.SHOW_TIME_DIALOG, 0));
    }

    public void onSaveClick(Location location) {
        if (event.getName().equals("")) return;

        // Add Event to Database
        eventRepository.addEvent(event);

        state.setValue(new State(ViewCall.SUCCESS, 0));
    }

    public String getName() {
        return event.getName();
    }

    public Location getLocation() {
        return event.getLocation();
    }

    public void setName(String name) {
        event.setName(name);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month,
                          int dayOfMonth) {
        switch (lastDateDialog) {
            case START_DIALOG:
                start.getValue().set(Calendar.YEAR, year);
                start.getValue().set(Calendar.MONTH, month);
                start.getValue().set(Calendar.DAY_OF_MONTH, dayOfMonth);
                event.setStart(start.getValue().getTime());
                start.postValue(start.getValue());
                break;
            case END_DIALOG:
                end.getValue().set(Calendar.YEAR, year);
                end.getValue().set(Calendar.MONTH, month);
                end.getValue().set(Calendar.DAY_OF_MONTH, dayOfMonth);
                event.setEnd(end.getValue().getTime());
                end.postValue(end.getValue());
                break;
        }

        lastDateDialog = NO_DIALOG;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (lastTimeDialog) {
            case START_DIALOG:
                start.getValue().set(Calendar.HOUR_OF_DAY, hourOfDay);
                start.getValue().set(Calendar.MINUTE, minute);
                event.setStart(start.getValue().getTime());
                start.postValue(start.getValue());
                break;
            case END_DIALOG:
                end.getValue().set(Calendar.HOUR_OF_DAY, hourOfDay);
                end.getValue().set(Calendar.MINUTE, minute);
                event.setEnd(end.getValue().getTime());
                end.postValue(end.getValue());
                break;
        }
        lastTimeDialog = NO_DIALOG;
    }
}
