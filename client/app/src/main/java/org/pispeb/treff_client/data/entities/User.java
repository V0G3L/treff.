package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.location.LocationManager;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Room {@link Entity} that represents a user, contains only as much information as the client needs
 */

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int userId;
    private String username;
    private boolean isFriend;
    private boolean isBlocked;
    private Location location;

    public User(int userId, String username, boolean isFriend, boolean
            isBlocked, Location location) {
        this.userId = userId;
        this.username = username;
        this.isFriend = isFriend;
        this.isBlocked = isBlocked;
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (isFriend != user.isFriend) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (location != null ? !location.equals(user.location) : user.location != null)
            return false;
        return isBlocked == user.isBlocked;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isFriend ? 1 : 0);
        result = 31 * result + (isBlocked ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
