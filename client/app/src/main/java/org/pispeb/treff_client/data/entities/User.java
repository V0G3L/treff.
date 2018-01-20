package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Room {@link Entity} that represents a user, contains only as much information as the client needs
 */

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int userID;
    private String username;
    private boolean isFriend;
    private boolean isBlocked;
    @Ignore //TODO position
    private Position position;
    @Ignore //TODO lastPositionUpdate;
    private LocalDateTime lastPositionUpdate;

    public User(int userID, String username, boolean isFriend, boolean isBlocked) {
        this.userID = userID;
        this.username = username;
        this.isFriend = isFriend;
        this.isBlocked = isBlocked;
        //this.position = position;
        //this.lastPositionUpdate = lastPositionUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userID != user.userID) return false;
        if (isFriend != user.isFriend) return false;
        if (isBlocked != user.isBlocked) return false;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int result = userID;
        result = 31 * result + username.hashCode();
        result = 31 * result + (isFriend ? 1 : 0);
        result = 31 * result + (isBlocked ? 1 : 0);
        return result;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

}
