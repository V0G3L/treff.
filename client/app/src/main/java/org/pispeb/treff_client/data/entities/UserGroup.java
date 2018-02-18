package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Room {@link Entity} that represents a group of {@link User}s
 */

@Entity(tableName = "usergroup")
public class UserGroup {
    @PrimaryKey
    private int groupId;
    private String name;
    private boolean isSharingLocation;

    public UserGroup(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.isSharingLocation = false;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSharingLocation() {
        return isSharingLocation;
    }

    public void setSharingLocation(boolean sharingLocation) {
        isSharingLocation = sharingLocation;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroup group = (UserGroup) o;

        if (groupId != group.groupId) return false;
        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + name.hashCode();
        return result;
    }
}
