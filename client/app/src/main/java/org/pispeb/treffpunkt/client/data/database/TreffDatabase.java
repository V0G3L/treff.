package org.pispeb.treffpunkt.client.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import org.pispeb.treffpunkt.client.data.entities.*;
import org.pispeb.treffpunkt.client.data.entities.converter.*;

/**
 * Local database storing all cached Data for entities.
 * To avoid desynchronization, TreffDataBase is implemented as a Singleton
 */

@Database(entities = {
        ChatMessage.class,
        Event.class,
        GroupMembership.class,
        User.class,
        UserGroup.class
        }, version = 12, exportSchema = false)
@TypeConverters({
        DateConverter.class,
        PositionConverter.class,
        IntegerSetConverter.class})
public abstract class TreffDatabase extends RoomDatabase {

    private static TreffDatabase INSTANCE;

    public static TreffDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, TreffDatabase.class, "treff.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract UserDao getUserDao();
    public abstract EventDao getEventDao();
    public abstract UserGroupDao getUserGroupDao();
    public abstract ChatDao getChatDao();
}
