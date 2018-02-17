package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Room {@link Entity} that represents the relationship between {@link User}s and {@link UserGroup}s
 */
@Entity(tableName = "groupmembership",
        primaryKeys = {"userId", "groupId"},
        foreignKeys = {
            @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId"),
            @ForeignKey(entity = UserGroup.class,
                        parentColumns = "groupId",
                        childColumns = "groupId")
        },
        indices={
            @Index(value="groupId"),
            @Index(value="userId")
        })
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
}
