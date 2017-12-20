package org.pispeb.treff_client.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;

import org.pispeb.treff_client.components.Event;
import org.pispeb.treff_client.components.User;
import org.pispeb.treff_client.components.UserGroup;

/**
 * Created by Lukas on 12/20/2017.
 */

@Database(entities = {User.class, UserGroup.class, Event.class}, version = 1)
public abstract class treffDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EventDao eventDao();
    public abstract UserGroupDao userGroupDao();
}
