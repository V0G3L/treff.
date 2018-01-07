package org.pispeb.treff_client.data.gps_handling;

import android.location.LocationListener;
import android.location.LocationManager;

import org.pispeb.treff_client.view.home.map.MapViewModel;
import org.pispeb.treff_client.data.networking.RequestEncoder;

/**
 * This class compares the positions given by the NETWORK_PROVIDER and the GPS_PROIDER.
 * The best position will be given to the listening mapViewModel and RequestEncoder.
 */

public class GPSProvider {

    LocationManager locationManager;
    LocationListener gpsListener;
    LocationManager networkListener;
    private RequestEncoder requestEncoder;
    private MapViewModel mapViewModel;
    private boolean isUpdating = false;


    /**
     * @param mapViewModel Sets the mapViewModel to recieve the current positions
     */
    public void subscribe(MapViewModel mapViewModel) {
        mapViewModel = mapViewModel;
    }


    /**
     * By calling this method the GroupMapViewModel won't get new positions.
     * If no GroupMapViewModel ist listening, nothing happens.
     */
    public void dismissMap() {}




    /**
     *
     * @param requestEncoder Sets the RequestEncoder to recieve the current positions
     */
    public void subsribe(RequestEncoder requestEncoder) {
        requestEncoder = requestEncoder;
    }


    /**
     * By calling this method the RequestEncoder won't get new positions.
     * If no RequestEncoder is listening, nothing happens.
     */
    public void dismissRequestEncoder() {}





}
