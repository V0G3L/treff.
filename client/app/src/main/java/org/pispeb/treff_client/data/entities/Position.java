package org.pispeb.treff_client.data.entities;

import java.time.LocalDateTime;

/**
 * Pojo for a users geographical position, given by latitude and longitude
 */

public class Position {

    private LocalDateTime lastActive;
    private double lat;
    private double lon;

    public Position(double lat, double lon, LocalDateTime lastActive) {
        this.lat = lat;
        this.lon = lon;
        this.lastActive = lastActive;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
