package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.User;


@Dao
public interface UserDao {
    @Insert
    void save(User user);

    @Query("SELECT * FROM user WHERE userID = :userID")
    LiveData<User> load(int userID);
}
