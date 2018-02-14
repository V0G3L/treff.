package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;
import java.util.Set;

/**
 * Room {@link Entity} that represents a group of {@link User}s
 */

@Entity(tableName = "usergroup")
public class UserGroup {
    @PrimaryKey(autoGenerate = true)
    private int groupId;
    private String name;
    private Set<Integer> events;
    private Set<Integer> polls;

    public UserGroup(String name, Set<Integer> events, Set<Integer> polls) {
        this.name = name;
        this.events = events;
        this.polls = polls;
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

    public Set<Integer> getPolls() {
        return polls;
    }

    public void setPolls(Set<Integer> polls) {
        this.polls = polls;
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
        if (events != null ? !events.equals(userGroup.events) : userGroup.events != null)
            return false;
        return polls != null ? polls.equals(userGroup.polls) : userGroup.polls == null;
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        result = 31 * result + (polls != null ? polls.hashCode() : 0);
        return result;
    }
}
