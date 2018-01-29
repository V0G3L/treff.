package org.pispeb.treff_client.view.home.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
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
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentMapBinding;
import org.pispeb.treff_client.view.util.State;

import java.util.ArrayList;
import java.util.List;

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
    String gpsProvider;
    String networkProvider;
    LocationManager locationManager;

    private ItemizedOverlayWithFocus overlay;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getActivity();
        //setting user agent to prevent getting banned from the osm servers
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        overlay = new ItemizedOverlayWithFocus(
                new ArrayList<OverlayItem>(),
                new ItemizedIconOverlay.OnItemGestureListener() {
                    @Override
                    public boolean onItemSingleTapUp(int index,
                                                     Object item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, Object item) {
                        return false;
                    }
                },
                getContext());
        overlay.setFocusItemsOnTap(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_map, container, false);
        vm = ViewModelProviders.of(this).get(MapViewModel.class);
        binding.setVm(vm);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));


        gpsProvider = LocationManager.GPS_PROVIDER;
        networkProvider = LocationManager.NETWORK_PROVIDER;
        locationManager = (LocationManager) getContext()
                .getSystemService(Context.LOCATION_SERVICE);

        vm.getState().observe(this, state -> callback(state));

        setLocationListener();

        //create map view and enable zooming
        MapView map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        //set default zoom level and location to show Karlsruhe, Germany
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(49.006889, 8.403653);
        mapController.setCenter(startPoint);
        map.getOverlays().add(overlay);

        // TODO get Overlay from vm
        vm.getItems().observe(this, (List<OverlayItem> overlayItems) -> {
            overlay.removeAllItems();
            overlay.addItems(overlayItems);
            map.invalidate();
        });

        return binding.getRoot();
    }

    private void setLocationListener() {
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
            Log.i("Map", "LocationListener set");
            // TODO define minimum update interval in config
            locationManager.requestLocationUpdates(gpsProvider, 0, 0,
                    vm);
            locationManager.requestLocationUpdates(networkProvider, 0, 0, vm);
        }
    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case CENTER_MAP:
                Log.i("Map", "Center on location");
                Location location = vm.getCurrentBestLocation();
                if (location != null) {
                    Log.i("Map", "location not null");
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
}
