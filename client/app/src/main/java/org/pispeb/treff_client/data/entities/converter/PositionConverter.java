package org.pispeb.treff_client.data.entities.converter;

import android.arch.persistence.room.TypeConverter;

import org.pispeb.treff_client.data.entities.Position;


public class PositionConverter {
    @TypeConverter
    public static Position toPosition(String position) {
        if (position == null) return null;
        String[] coords = position.split("#");
        double lat = Double.parseDouble(coords[0]);
        double lon = Double.parseDouble(coords[1]);
        return new Position(lat, lon);
    }

    @TypeConverter
    public static String toDoubles(Position position) {
        if (position == null) return null;
        return position.getLat() + "#" + position.getLon();
    }
}
