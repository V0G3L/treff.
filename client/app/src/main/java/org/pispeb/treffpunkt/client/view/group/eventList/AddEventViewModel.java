package org.pispeb.treffpunkt.client.view.group.eventList;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.location.LocationManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

import java.util.Calendar;
import java.util.Date;

/**
 * ViewModel for creating a new event
 */

public class AddEventViewModel extends ViewModel
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    // not to be modified
    private int id;

    private EventRepository eventRepository;
    private UserGroupRepository userGroupRepository;

    private String name;
    private Location location;

    private MutableLiveData<Calendar> start;
    private MutableLiveData<Calendar> end;

    int groupId;

    private SingleLiveEvent<State> state;

    private int lastTimeDialog = NO_DIALOG;
    private int lastDateDialog = NO_DIALOG;
    private final static int NO_DIALOG = 0;
    private final static int START_DIALOG = 1;
    private final static int END_DIALOG = 2;

    private int mode = CREATE;
    private final static int CREATE = 0;
    private final static int EDIT = 1;


    public AddEventViewModel(EventRepository eventRepository,
                             UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;

        start = new MutableLiveData<>();
        end = new MutableLiveData<>();
        start.setValue(Calendar.getInstance());
        end.setValue(Calendar.getInstance());
        end.getValue().add(Calendar.HOUR, 1);

        location = new Location(LocationManager.GPS_PROVIDER);
        name = "";

        state = new SingleLiveEvent<>();
    }

    public void setupForEdit(int id, int groupId, String name, Location l,
                             Date start, Date end) {
        mode = EDIT;
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.location = l;
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        this.start.postValue(c);
        Calendar e = Calendar.getInstance();
        e.setTime(end);
        this.end.postValue(e);
    }

    public void setupForCreate(int groupId) {
        mode = CREATE;
        this.groupId = groupId;
    }

    public MutableLiveData<Calendar> getStart() {
        return start;
    }

    public MutableLiveData<Calendar> getEnd() {
        return end;
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
        if (name.equals("")) return;

        if (mode == CREATE) {
            // Add Event to Database
            eventRepository.requestAddEvent(
                    groupId,
                    name,
                    start.getValue().getTime(),
                    end.getValue().getTime(),
                    location);
        } else {
            eventRepository.requestEditEvent(
                    id,
                    groupId,
                    name,
                    start.getValue().getTime(),
                    end.getValue().getTime(),
                    location
            );
        }
        state.setValue(new State(ViewCall.SUCCESS, 0));
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month,
                          int dayOfMonth) {
        switch (lastDateDialog) {
            case START_DIALOG:
                start.getValue().set(Calendar.YEAR, year);
                start.getValue().set(Calendar.MONTH, month);
                start.getValue().set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start.postValue(start.getValue());
                break;
            case END_DIALOG:
                end.getValue().set(Calendar.YEAR, year);
                end.getValue().set(Calendar.MONTH, month);
                end.getValue().set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
                start.postValue(start.getValue());
                break;
            case END_DIALOG:
                end.getValue().set(Calendar.HOUR_OF_DAY, hourOfDay);
                end.getValue().set(Calendar.MINUTE, minute);
                end.postValue(end.getValue());
                break;
        }
        lastTimeDialog = NO_DIALOG;
    }
}
