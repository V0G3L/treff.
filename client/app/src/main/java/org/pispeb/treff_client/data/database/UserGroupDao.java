package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;

import java.util.List;

/**
 * {@link Dao} which provides access to {@link UserGroup}s
 */

@Dao
public interface UserGroupDao {
    @Insert
    void save(UserGroup group);

    @Query("SELECT * FROM usergroup WHERE groupID = :groupId")
    LiveData<UserGroup> getGroupByID(int groupId);

    @Query("SELECT * FROM usergroup")
    LiveData<List<UserGroup>> getGroups();

    @Insert
    void save(GroupMembership groupMembership);

    @Query("SELECT usergroup.groupID, name " +
            "FROM groupmembership INNER JOIN usergroup " +
            "ON groupmembership.groupID = usergroup.groupID " +
            "WHERE groupmembership.userID = :userID")
    LiveData<List<UserGroup>> getGroupsByUser(int userID);

    @Query("SELECT user.userID, username, isFriend, isBlocked " +
            "FROM groupmembership INNER JOIN user " +
            "ON groupmembership.userID = user.userID " +
            "WHERE groupmembership.groupID = :groupID")
    LiveData<List<User>> getUsersByGroup(int groupID);
}
