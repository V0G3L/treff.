package org.pispeb.treffpunkt.client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.pispeb.treffpunkt.client.data.entities.GroupMembership;
import org.pispeb.treffpunkt.client.data.entities.UserGroup;

import java.util.List;

/**
 * {@link Dao} which provides access to {@link UserGroup}s
 */

@Dao
public interface UserGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(UserGroup group);

    @Delete
    void delete(UserGroup... userGroups);

    @Query("SELECT * FROM usergroup WHERE groupID = :groupId")
    LiveData<UserGroup> getGroupLiveDataById(int groupId);

    @Query("SELECT * FROM usergroup WHERE groupID = :groupId")
    UserGroup getGroupById(int groupId);

    @Query("SELECT * FROM usergroup")
    DataSource.Factory<Integer, UserGroup> getAllGroups();

    @Query("SELECT * FROM usergroup")
    LiveData<List<UserGroup>> getAllGroupsInList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(GroupMembership groupMembership);

    @Delete
    void delete(GroupMembership groupMembership);

    @Update
    void update(GroupMembership groupMembership);

    @Query("DELETE FROM groupmembership WHERE groupId = :groupId")
    void deleteMembershipsOfGroup(int groupId);

    @Query("SELECT * FROM groupmembership WHERE groupId = :groupId")
    List<GroupMembership> getGroupMembershipsByGroupId(int groupId);

    @Query("SELECT * FROM groupmembership WHERE groupId = :groupId")
    LiveData<List<GroupMembership>> getGroupMembershipsByGroupIdLiveData(int groupId);

    @Query("UPDATE usergroup SET isSharingLocation = :isSharing WHERE groupId" +
            " = :groupId")
    void setIsSharing(int groupId, boolean isSharing);


    @Delete
    void deleteGroupMemberships(List<GroupMembership> groupMemberships);

    @Update
    void update(UserGroup userGroup);

    @Query("DELETE FROM usergroup")
    void deleteAllGroups();

    @Query("DELETE FROM groupmembership")
    void deleteAllMemberships();
}
