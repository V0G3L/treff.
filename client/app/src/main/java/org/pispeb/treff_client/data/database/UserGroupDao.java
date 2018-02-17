package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;

import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

/**
 * {@link Dao} which provides access to {@link UserGroup}s
 */

@Dao
public interface UserGroupDao {
    @Insert
    void save(UserGroup group);

    @Delete
    void delete(UserGroup... userGroups);

    @Delete
    void deleteGroups(UserGroup... userGroups);

    @Query("SELECT * FROM usergroup WHERE groupID = :groupId")
    LiveData<UserGroup> getGroupByID(int groupId);

    @Query("SELECT * FROM usergroup")
    DataSource.Factory<Integer, UserGroup> getAllGroups();

    @Query("SELECT * FROM usergroup")
    LiveData<List<UserGroup>> getAllGroupsInList();

    @Insert
    void save(GroupMembership groupMembership);

    @Delete
    void delete(GroupMembership groupMembership);

    @Query("SELECT usergroup.groupId, name " +
            "FROM groupmembership INNER JOIN usergroup " +
            "ON groupmembership.groupId = usergroup.groupId " +
            "WHERE groupmembership.userId = :userId")
    DataSource.Factory<Integer, UserGroup> getGroupsByUser(int userId);

    @Query("SELECT user.userId, user.username, user.isFriend, user.isBlocked " +
            "FROM groupmembership INNER JOIN user " +
            "ON groupmembership.userId = user.userId " +
            "WHERE groupmembership.groupId = :groupId")
    DataSource.Factory<Integer, User> getUsersByGroup(int groupId);


    @Query("SELECT * FROM groupmembership WHERE groupId = :groupId")
    List<GroupMembership> getGroupMembershipsByGroupId(int groupId);


    @Delete
    void deleteGroupMemberships(List<GroupMembership> groupMemberships);

    @Update
    void update(UserGroup userGroup);
}
