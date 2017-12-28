package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.UserGroup;

/**
 * Created by Lukas on 12/20/2017.
 */

@Dao
public interface UserGroupDao {
    @Insert
    void save(UserGroup group);

    @Query("SELECT * FROM  WHERE id = :groupId")
    LiveData<UserGroup> load(int groupId);
}
