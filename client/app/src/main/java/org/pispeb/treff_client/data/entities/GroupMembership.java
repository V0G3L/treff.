package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Room {@link Entity} that represents the relationship between {@link User}s and {@link UserGroup}s
 */
@Entity(tableName = "groupmembership")
public class GroupMembership {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userID;
    private int groupID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
