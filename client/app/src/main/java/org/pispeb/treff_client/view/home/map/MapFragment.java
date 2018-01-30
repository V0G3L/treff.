package org.pispeb.treff_client.view.home.map;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentMapBinding;
import org.pispeb.treff_client.view.util.State;


/**
 * Displaying the Map and other users position
 */

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MapViewModel vm;

    private static final int REQUEST_LOCATION = 1;
    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private LocationManager locationManager;

    private Marker userMarker;
    private MapView map;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Map", "create View");

        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_map, container, false);
        vm = ViewModelProviders.of(this).get(MapViewModel.class);
        binding.setVm(vm);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

        setLocationListener();

        configureMap();

        configureUserMarker();

        vm.getState().observe(this, state -> callback(state));

        vm.getUserLocation().observe(this, userLocation ->
                updateUserLocation(userLocation));


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        Log.i("Map", "destroy View");
        super.onDestroyView();
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
                    LocationManager.NETWORK_PROVIDER, 0,0, vm);
        }
    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case CENTER_MAP:
                // Log.i("Map", "Center on location");
                Location location = vm.getCurrentBestLocation();
                if (location != null) {
                    // Log.i("Map", "location not null");
                    GeoPoint currentPoint = new GeoPoint(location);
                    binding.map.getController().setCenter(currentPoint);
                }
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

        //set default zoom level and location to show Karlsruhe, Germany
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(49.006889, 8.403653);
        mapController.setCenter(startPoint);
    }

    private void configureUserMarker() {
        userMarker = new Marker(map);
        userMarker.setIcon(getResources().getDrawable(R.drawable
                .ic_marker_own, getContext().getTheme()));
        userMarker.setTitle("Your only friend: You");
        map.getOverlays().add(userMarker);
    }

    private void updateUserLocation(Location userLocation) {
        userMarker.setPosition(new GeoPoint(userLocation));
        // TODO rotate
        map.invalidate();
    }
}
