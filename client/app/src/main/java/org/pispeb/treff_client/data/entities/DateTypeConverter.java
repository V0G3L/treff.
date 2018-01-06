package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDateTime;

public class DateTypeConverter {

    @TypeConverter
    public static LocalDateTime toDate(Long timestamp) {
        //TODO implement
        return null;
    }

    @TypeConverter
    public static Long toTimestamp(LocalDateTime date) {
        //TODO implement
        return null;
    }

}
