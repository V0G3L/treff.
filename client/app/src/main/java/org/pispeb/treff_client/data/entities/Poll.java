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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        if (pollID != poll.pollID) return false;
        return options.equals(poll.options);
    }

    @Override
    public int hashCode() {
        int result = pollID;
        result = 31 * result + options.hashCode();
        return result;
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
