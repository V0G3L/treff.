package org.pispeb.treff_client.view.home.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Keeps track of filter options etc.
 */

public class MapViewModel extends ViewModel implements LocationListener {

    // indicator of noticeable delay to last location
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    // Repositories to fetch data from db
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;

    // Livedata objects to keep map up to date
    private MutableLiveData<Location> userLocation;
    private LiveData<PagedList<User>> friends;
    private LiveData<PagedList<Event>> events;
    private LiveData<List<UserGroup>> groups;

    private Set<UserGroup> activeGroups;

    private SingleLiveEvent<State> state;


    public MapViewModel(
            UserGroupRepository userGroupRepository,
            UserRepository userRepository,
            EventRepository eventRepository) {

        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;

        this.userLocation = new MutableLiveData<>();
        this.events = eventRepository.getEvents();
        this.groups = userGroupRepository.getGroupsInList();
        this.friends = userRepository.getFriends();

        this.activeGroups = new HashSet<>();

        this.state = new SingleLiveEvent<>();
    }

    public void onCenterClick() {
        // set map center to current best location
        state.setValue(new State(ViewCall.CENTER_MAP, 0));
    }

    public void onFilterClick() {
        state.setValue(new State(ViewCall.SHOW_FILTER_DIALOG, 0));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(userLocation.getValue(), location)) {
            userLocation.postValue(location);
        }
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public MutableLiveData<Location> getUserLocation() {
        return userLocation;
    }

    public LiveData<PagedList<User>> getFriends() {
        return friends;
    }

    public LiveData<PagedList<Event>> getEvents() {
        return events;
    }

    public LiveData<List<UserGroup>> getGroups() {
        return groups;
    }

    public Set<UserGroup> getActiveGroups() {
        return activeGroups;
    }

    public void setActiveGroups(Set<UserGroup> activeGroups) {
        this.activeGroups = activeGroups;
        events = eventRepository.getEventsFromGroups(activeGroups);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Compares given location to current best location in terms of accuracy,
     * delay and provider credibility.
     * @param location new, alternative location
     * @return true if given location is "better"
     */
    private boolean isBetterLocation(Location currentBestLocation, Location
                                     location) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location,
        // use the new location because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it
            // must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location
                .getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate &&
                isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
