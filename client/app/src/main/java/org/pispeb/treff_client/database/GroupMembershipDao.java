package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.User;
import org.pispeb.treff_client.entities.UserGroup;

import java.util.List;

@Dao
public interface GroupMembershipDao {
    @Insert
    void save(int groupID, int userID);

    @Query("SELECT groupID FROM GroupMembership WHERE userID = :userID")
    LiveData<List<UserGroup>> getGroupsByUser(int userID);

    @Query("SELECT userID FROM GroupMembership WHERE groupID = :groupID")
    LiveData<List<User>> getUsersByGroup(int groupID);
}
