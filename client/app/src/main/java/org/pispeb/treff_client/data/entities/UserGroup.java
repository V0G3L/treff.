package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Room {@link Entity} that represents a group of {@link User}s
 */

@Entity(tableName = "usergroup")
public class UserGroup {
    @PrimaryKey
    private int groupID;
    private String name;
    //TODO replace
    //private List<Integer> eventIDs;

    public UserGroup(String name, int groupID) {
        this.name = name;
        this.groupID = groupID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroup userGroup = (UserGroup) o;

        if (groupID != userGroup.groupID) return false;
        return name.equals(userGroup.name);
    }

    @Override
    public int hashCode() {
        int result = groupID;
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

}
