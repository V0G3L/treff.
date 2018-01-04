package org.pispeb.treff_client.entities;

import java.time.LocalDateTime;


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
