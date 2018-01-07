package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.TypeConverter;


public class PositionConverter {
    @TypeConverter
    public static Position toPosition(double lon, double lat) {
        //TODO implement
        return null;
    }

    @TypeConverter
    public static double[] toDoubles(Position position) {
        //TODO implement
        return null;
    }
}
