package org.pispeb.treff_client.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.content.Context;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;

/**
 * Local database storing all cached Data for entities.
 * To avoid desynchronization, TreffDataBase is implemented as a Singleton
 */

@Database(entities = {User.class, UserGroup.class, Event.class, GroupMembership.class}, version = 2, exportSchema = false)
public abstract class TreffDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    public abstract EventDao getEventDao();
    public abstract UserGroupDao getUserGroupDao();
    public abstract GroupMembershipDao getGroupmembershipDao();

}
