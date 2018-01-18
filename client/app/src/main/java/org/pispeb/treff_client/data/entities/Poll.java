package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Room {@link Entity} that represents a poll
 */

@Entity(tableName = "poll")
public class Poll extends Occasion {
    @PrimaryKey
    private int pollID;
    @Ignore //TODO options
    private List<PollOption> options;

    public Poll(int pollID, String name) {
        this.pollID = pollID;
        this.name = name;
        //this.options = options;
    }

    public int getPollID() {
        return pollID;
    }

    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }
}
