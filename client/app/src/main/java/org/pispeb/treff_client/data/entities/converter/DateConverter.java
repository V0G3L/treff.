package org.pispeb.treff_client.data.entities.converter;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Contains TypeConverters to allow conversion
 * from {@link Date}s to Unix Timestamps and vice versa
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
