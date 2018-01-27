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
    private int userId;
    private int groupId;

    public GroupMembership(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
