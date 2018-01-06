package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.UserGroup;

/**
 * Dao to save and load groups to/from local database
 */

@Dao
public interface UserGroupDao {
    @Insert
    void save(UserGroup group);

    @Query("SELECT * FROM usergroup WHERE groupID = :groupId")
    LiveData<UserGroup> getGroupByID(int groupId);
}
