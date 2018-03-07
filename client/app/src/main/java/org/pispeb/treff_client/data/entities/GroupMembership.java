package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;

/**
 * Room {@link Entity} that represents the relationship between {@link User}s and {@link UserGroup}s
 */
@Entity(tableName = "groupmembership",
        primaryKeys = {"userId", "groupId"})
public class GroupMembership {
    private int userId;
    private int groupId;

    public GroupMembership(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembership that = (GroupMembership) o;

        if (userId != that.userId) return false;
        return groupId == that.groupId;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + groupId;
        return result;
    }
}
