package org.pispeb.treffpunkt.server.service.domain;

public class Position {

    private double latitude;
    private double longitude;
    private long timeMeasured;

    public Position(double latitude, double longitude, long timeMeasured) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeMeasured = timeMeasured;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimeMeasured() {
        return timeMeasured;
    }

    public void setTimeMeasured(long timeMeasured) {
        this.timeMeasured = timeMeasured;
    }
}
