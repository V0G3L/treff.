package org.pispeb.treff_server;

import java.time.DateTimeException;
import java.util.Date;

public class Position {

    public final double latitude;
    public final double longitude;
    public final Date timeMeasured;

    public Position(double latitude, double longitude, Date timeMeasured) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeMeasured = timeMeasured;
    }
}
