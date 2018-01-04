package org.pispeb.treff_client.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;

import org.pispeb.treff_client.entities.Event;
import org.pispeb.treff_client.entities.GroupMembership;
import org.pispeb.treff_client.entities.User;
import org.pispeb.treff_client.entities.UserGroup;


@Database(entities = {User.class, UserGroup.class, Event.class, GroupMembership.class}, version = 1)
public abstract class treffDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract EventDao getEventDao();
    public abstract UserGroupDao getUserGroupDao();
    public abstract GroupMembership getGroupmembershipDao();
}
