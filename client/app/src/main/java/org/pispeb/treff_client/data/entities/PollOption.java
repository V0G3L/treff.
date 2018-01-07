package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Option in a poll that users of the group can vote for
 */

@Entity(tableName = "polloption")
public class PollOption {
    @PrimaryKey
    private int optionID;
    private String name;
    @Ignore //TODO position
    private Position position;

    public PollOption(int optionID, String name) {
        this.optionID = optionID;
        this.name = name;
        //this.position = position;
    }

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
