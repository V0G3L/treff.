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
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String username;
    private boolean isFriend;
    private boolean isBlocked;
    private Position position;
    private Date lastPositionUpdate;

    public User(String username, boolean isFriend, boolean isBlocked, Position position, Date lastPositionUpdate) {
        this.username = username;
        this.isFriend = isFriend;
        this.isBlocked = isBlocked;
        this.position = position;
        this.lastPositionUpdate = lastPositionUpdate;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getLastPositionUpdate() {
        return lastPositionUpdate;
    }

    public void setLastPositionUpdate(Date lastPositionUpdate) {
        this.lastPositionUpdate = lastPositionUpdate;
    }

    public Location getLocation() {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(position.getLat());
        l.setLongitude(position.getLon());
        l.setTime(lastPositionUpdate.getTime());
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (isFriend != user.isFriend) return false;
        if (isBlocked != user.isBlocked) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (position != null ? !position.equals(user.position) : user.position != null)
            return false;
        return lastPositionUpdate != null ? lastPositionUpdate.equals(user.lastPositionUpdate) : user.lastPositionUpdate == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isFriend ? 1 : 0);
        result = 31 * result + (isBlocked ? 1 : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (lastPositionUpdate != null ? lastPositionUpdate.hashCode() : 0);
        return result;
    }
}
