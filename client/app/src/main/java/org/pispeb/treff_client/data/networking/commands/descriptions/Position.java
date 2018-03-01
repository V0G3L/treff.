package org.pispeb.treff_client.data.networking.commands.descriptions;

import android.location.Location;
import android.location.LocationManager;

public class Position {

    public final double latitude;
    public final double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean equals(Position position) {
        return (this.latitude == position.latitude && this.longitude == position.longitude);
    }

    public Location getLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(this.longitude);
        location.setLatitude(this.latitude);
        return location;
    }
}
