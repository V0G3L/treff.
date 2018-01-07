package org.pispeb.treff_client.data.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;

import org.pispeb.treff_client.data.entities.*;

/**
 * Local database storing all cached Data for entities.
 * To avoid desynchronization, TreffDataBase is implemented as a Singleton
 */

@Database(entities = {
        Chat.class,
        ChatMessage.class,
        Event.class,
        GroupMembership.class,
        Poll.class,
        PollOption.class,
        User.class,
        UserGroup.class
        }, version = 2, exportSchema = false)
public abstract class TreffDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    public abstract EventDao getEventDao();
    public abstract UserGroupDao getUserGroupDao();

}
