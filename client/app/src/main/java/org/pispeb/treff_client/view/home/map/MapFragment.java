package org.pispeb.treff_client.view.home.map;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.databinding.FragmentMapBinding;
import org.pispeb.treff_client.view.home.map.markers.EventMarker;
import org.pispeb.treff_client.view.home.map.markers.UserMarker;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Displaying the Map and other users position
 */

public class MapFragment extends Fragment {

    public static final int STANDARD_ZOOM_LEVEL = 15;
    private FragmentMapBinding binding;
    private MapViewModel vm;

    private static final int REQUEST_LOCATION = 1;
    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private LocationManager locationManager;

    private MapView map;
    private FolderOverlay master;
    private FolderOverlay pollOptionMarkers;
    private FolderOverlay eventMarkers;
    // TODO replace with RadiusMarkerClusterer
    private RadiusMarkerClusterer contactMarkers;
    private Marker userMarker;

    private final static int POLLS = 0;
    private final static int EVENTS = 1;
    private final static int CONTACTS = 2;
    private final static int USER = 3;
    private List<UserGroup> groupList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("Map", "create");

        Context ctx = getActivity();
        //setting user agent to prevent getting banned from the osm servers
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
    }

    @Override
    public void onStart() {
        super.onStart();
        setLocationListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Map", "create View");

        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_map, container, false);
        vm = ViewModelProviders.of(this,
                ViewModelFactory.getInstance(getContext()))
                .get(MapViewModel.class);
        binding.setVm(vm);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

        configureMap();

        configureUserMarker();

        vm.getState().observe(this, state -> callback(state));

        vm.getUserLocation().observe(this, userLocation ->
                updateUserLocation(userLocation));

        vm.getFriends().observe(this, friends ->
                updateContactLocations(friends));

        vm.getEvents().observe(this, events ->
                updateEventLocations(events));

        groupList = new ArrayList<>();
        vm.getGroups().observe(this, groups -> {
            groupList = groups;
        });

        //make sure the map catches lateral swipes instead of the viewpager
        disableTouchTheft(binding.map);

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(vm);
        vm.getUserLocation().removeObservers(this);
    }

    private void setLocationListener() {
        locationManager = (LocationManager) getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        // check if Permission to use GPS is granted
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // send request for permissions
            requestPermissions(
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION);
        } else {
            // Log.i("Map", "LocationListener set");
            // TODO define minimum update interval in config
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, vm);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, vm);
        }
    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case CENTER_MAP:
                // Log.i("Map", "Center on location");
                Location location = vm.getUserLocation().getValue();
                if (location != null) {
                    // Log.i("Map", "location not null");
                    GeoPoint currentPoint = new GeoPoint(location);
                    binding.map.getController().setCenter(currentPoint);
                    binding.map.getController().setZoom(STANDARD_ZOOM_LEVEL);
                }
                break;
            case SHOW_FILTER_DIALOG:
                showFilterDialog();
                break;
            default:
                Log.i("Map", "Illegal VM State");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setLocationListener();
            }
        }
    }

    private void configureMap() {
        //create map view and enable zooming
        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);

        //set default zoom level and location to show Karlsruhe, Germany
        IMapController mapController = map.getController();
        mapController.setZoom(STANDARD_ZOOM_LEVEL);
        map.setMinZoomLevel(4);
        GeoPoint startPoint = new GeoPoint(49.006889, 8.403653);
        mapController.setCenter(startPoint);


        master = new FolderOverlay();
        pollOptionMarkers = new FolderOverlay();
        eventMarkers = new FolderOverlay();
        contactMarkers = new RadiusMarkerClusterer(getContext());
        Drawable groupIcon = getResources().getDrawable(R.drawable
                .marker_group, getContext().getTheme());

        Bitmap clusterIcon = ((BitmapDrawable) groupIcon).getBitmap();
        contactMarkers.setIcon(clusterIcon);
        userMarker = new Marker(map);

        master.add(pollOptionMarkers);
        master.add(eventMarkers);
        master.add(contactMarkers);
        master.add(userMarker);

        map.getOverlays().add(master);
    }

    private void configureUserMarker() {
        userMarker.setIcon(getResources().getDrawable(R.drawable
                .ic_marker_own, getContext().getTheme()));
        userMarker.setTitle("Your only friend: You");
        map.invalidate();
    }

    private void updateUserLocation(Location userLocation) {
        userMarker.setPosition(new GeoPoint(userLocation));
        // TODO rotate
        map.invalidate();
    }

    private void updateContactLocations(List<User> contacts) {
        // TODO replace with nicer update algorithm
        ((RadiusMarkerClusterer) master.getItems().get(CONTACTS))
                .getItems().clear();
        List<UserMarker> markers = new ArrayList<>();
        for (User u : contacts) {
            UserMarker m = new UserMarker(map, u);
            m.setIcon(getResources().getDrawable(R.drawable.ic_marker_person,
                    getContext().getTheme()));
            markers.add(m);
        }
        ((RadiusMarkerClusterer) master.getItems().get(CONTACTS))
                .getItems().addAll(markers);
    }

    private void updateEventLocations(List<Event> events) {
        // TODO replace with nicer update algorithm
        ((FolderOverlay) master.getItems().get(EVENTS)).getItems().clear();
        List<EventMarker> markers = new ArrayList<>();
        for (Event e : events) {
            EventMarker m = new EventMarker(map, e);
            m.setIcon(getResources().getDrawable(R.drawable.ic_marker_event,
                    getContext().getTheme()));
            m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            markers.add(m);
        }
        ((FolderOverlay) master.getItems().get(EVENTS)).getItems().addAll
                (markers);
    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Array of all groups
        UserGroup[] groups = new UserGroup[groupList.size()];
        groupList.toArray(groups);
        // Set of active groups
        Set<Integer> activeGroups = vm.getActiveGroups();
        // Groupnames displayed in dialog
        String[] names = new String[groupList.size()];
        // boolean for which groups are currently shown
        boolean[] checkedGroups = new boolean[groupList.size()];

        // set names and booleans
        for (int i = 0; i < groups.length; i++) {
            names[i] = groups[i].getName();
            if (activeGroups.contains(groups[i])) {
                checkedGroups[i] = true;
            }
        }

        builder.setMultiChoiceItems(names, checkedGroups,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        // check/uncheck item
                        checkedGroups[which] = isChecked;
                        //TODO does not handle changing groups during dialog
                        UserGroup group = groups[which];
                        // set corresponding group to be (un)active
                        if (isChecked) {
                            activeGroups.add(group.getGroupId());
                        } else {
                            activeGroups.remove(group.getGroupId());
                        }
                    }
                });

        builder.setTitle("Displayed Groups:");

        // ok button closes dialog and saves changes to vm
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            vm.setActiveGroups(activeGroups);
            dialog.dismiss();
        });

        // display the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     * Ensures that a view correctly catches touch events
     * by disabling their parent view's onInterceptTouchEvent
     *
     * @param target the view to catch touch events
     */
    private static void disableTouchTheft(View target) {
        target.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);

                if ((motionEvent
                        .getAction() & MotionEvent.ACTION_MASK) ==
                        MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
    }

}

