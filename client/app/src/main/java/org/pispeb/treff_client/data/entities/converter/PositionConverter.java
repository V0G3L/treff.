package org.pispeb.treff_client.data.entities.converter;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;
import android.location.LocationManager;


public class PositionConverter {
    @TypeConverter
    public static Location toLocation(String location) {
        if (location == null) return null;
        String[] split = location.split("#");
        double lat = Double.parseDouble(split[0]);
        double lon = Double.parseDouble(split[1]);
        long time = Long.parseLong(split[2]);
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(lat);
        l.setLongitude(lon);
        l.setTime(time);
        return l;
    }

    @TypeConverter
    public static String toDoubles(Location location) {
        if (location == null) return null;
        return location.getLatitude() + "#"
                + location.getLongitude() + "#"
                + location.getTime();
    }
}
