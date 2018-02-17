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
    private Set<Integer> events;
    //TODO set of users in group
    // private Set<Integer> members

//    private Set<Integer> polls;

    public UserGroup(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.events = new HashSet<>();
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

    public Set<Integer> getEvents() {
        return events;
    }

    public void setEvents(Set<Integer> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroup userGroup = (UserGroup) o;

        if (groupId != userGroup.groupId) return false;
        if (name != null ? !name.equals(userGroup.name) : userGroup.name != null) return false;
        return (events != null ? !events.equals(userGroup.events) : userGroup
                .events != null);
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        return result;
    }
}
