package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.pispeb.treff_client.data.entities.User;
import java.util.List;


/**
 * {@link Dao} which provides access to {@link User}s
 */

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(User user);

    @Delete()
    void deleteAll(List<User> users);

    @Query("UPDATE user SET isFriend = 0, requestPending = 0, isRequesting = " +
            "0 WHERE userId = :userId")
    void reset(int userId);

    @Query("SELECT * FROM user WHERE userID = :userID")
    User getUserById(int userID);

    @Query("SELECT * FROM user WHERE userID = :userID")
    LiveData<User> getUserByIdLiveData(int userID);

    @Query("SELECT * FROM user WHERE username = :username")
    User getUserByName(String username);

    @Query("SELECT * FROM user WHERE userID IN(:userIds)")
    DataSource.Factory<Integer, User> getUsersByIdArray(int[] userIds);

    @Update
    void update(User user);

    @Query("SELECT * FROM user WHERE isFriend & NOT isBlocked ORDER BY " +
            "username")
    DataSource.Factory<Integer, User> getFriends();

    @Query("SELECT * FROM user WHERE (isFriend | requestPending | " +
            "isRequesting) & NOT isBlocked ORDER BY isBlocked, username")
    DataSource.Factory<Integer, User> getFriendsAndPending();

    @Query("SELECT * FROM user WHERE (isFriend | requestPending | " +
            "isRequesting | isBlocked) ORDER BY isBlocked, username")
    DataSource.Factory<Integer, User> getAll();

    @Query("SELECT * FROM user WHERE isFriend & NOT isBlocked")
    LiveData<List<User>> getFriendsAsList();

    @Query("SELECT * FROM user")
    List<User> getAllAsList();

    @Query("UPDATE user SET isBlocked = :isBlocked WHERE userId = :userId")
    void setBlocked(int userId, boolean isBlocked);

    @Query("UPDATE user SET isFriend = :isFriend WHERE userId = :userId")
    void setIsFriend(int userId, boolean isFriend);

    @Query("UPDATE user SET requestPending = :isPending WHERE userId = :userId")
    void setIsPending(int userId, boolean isPending);

    @Query("UPDATE user SET isRequesting = :isRequesting WHERE userId = " +
            ":userId")
    void setIsRequesting(int userId, boolean isRequesting);

    @Query("UPDATE user SET username = :name WHERE userId = :userId")
    void setUserName(int userId, String name);

}
