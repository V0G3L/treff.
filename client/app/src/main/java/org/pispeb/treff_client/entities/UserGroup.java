package org.pispeb.treff_client.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Group of users in which events and polls can be created
 * and chat messages can be sent
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
