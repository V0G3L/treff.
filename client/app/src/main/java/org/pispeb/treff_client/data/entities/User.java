package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Pojo for information about a user that is relevant for the client
 * (private information such as e-mail is not passed on to the client)
 */

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int userID;
    private String username;
    private boolean isFriend;
    private boolean isBlocked;
    //TODO replace
    //private Position position;

    public User(int userID, String username, boolean isFriend, boolean isBlocked) {
        this.userID = userID;
        this.username = username;
        this.isFriend = isFriend;
        this.isBlocked = isBlocked;
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

    /*TODO fix
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    */
}
