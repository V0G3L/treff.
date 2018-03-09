package org.pispeb.treff_client.data.networking.commands.descriptions;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {

    public final double latitude;
    public final double longitude;

    public Position(@JsonProperty("latitude") double latitude,
                    @JsonProperty("longitude") double longitude) {
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
