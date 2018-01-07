package org.pispeb.treff_client.data.entities.converter;

import android.arch.persistence.room.TypeConverter;

import org.pispeb.treff_client.data.entities.Position;


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
