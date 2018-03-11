package org.pispeb.treffpunkt.client.data.entities;

import android.arch.persistence.room.Entity;

import java.util.Date;

/**
 * Room {@link Entity} that represents the relationship between {@link User}s
 * and {@link UserGroup}s
 */
@Entity(tableName = "groupmembership",
        primaryKeys = {"userId", "groupId"})
public class GroupMembership {
    private int userId;
    private int groupId;
    private Date sharingUntil;
    private boolean sharing;

    public GroupMembership(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
        this.sharingUntil = new Date(0L);
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

    public Date getSharingUntil() {
        return sharingUntil;
    }

    public void setSharingUntil(Date sharingUntil) {
        this.sharingUntil = sharingUntil;
    }

    public boolean isSharing() {
        return sharing;
    }

    public void setSharing(boolean sharing) {
        this.sharing = sharing;
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
