package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.GroupMembership;
import org.pispeb.treff_client.entities.User;
import org.pispeb.treff_client.entities.UserGroup;

import java.util.List;

@Dao
public interface GroupMembershipDao {
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
