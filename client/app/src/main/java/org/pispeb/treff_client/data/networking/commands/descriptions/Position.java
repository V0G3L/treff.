package org.pispeb.treff_client.data.networking.commands.descriptions;

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
}
