package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.User;

import java.util.List;


@Dao
public interface UserDao {
    @Insert
    void save(User user);

    @Query("SELECT * FROM user WHERE userID = :userID")
    LiveData<User> getUserByID(int userID);

    @Query("SELECT * FROM user WHERE isFriend = 1")
    LiveData<List<User>> getFriends();

}
