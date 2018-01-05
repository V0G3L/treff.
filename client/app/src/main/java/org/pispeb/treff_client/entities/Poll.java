package org.pispeb.treff_client.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Pojo containing information of a poll in a group and
 * all its possible options for users to vote for
 */

@Entity(tableName = "poll")
public class Poll {
    @PrimaryKey
    private int pollID;
    private String name;
    private List<PollOption> options;

    public Poll(int pollID, String name, List<PollOption> options) {
        this.pollID = pollID;
        this.name = name;
        this.options = options;
    }

    public int getPollID() {
        return pollID;
    }

    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }
}
