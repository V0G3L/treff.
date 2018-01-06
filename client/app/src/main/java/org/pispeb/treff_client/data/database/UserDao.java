package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.User;
import java.util.List;

/**
 * Dao for saving userdata to the database and
 * loading users based on id/group memberships/friend status etc.
 */

@Dao
public interface UserDao {
    @Insert
    void save(User user);

    @Query("SELECT * FROM user WHERE userID = :userID")
    LiveData<User> getUserByID(int userID);

    @Query("SELECT * FROM user WHERE isFriend = 1")
    LiveData<List<User>> getFriends();

}
