package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;
import java.util.Set;

/**
 * Room {@link Entity} that represents a poll
 */

@Entity(tableName = "poll")
public class Poll extends Occasion {
    @PrimaryKey(autoGenerate = true)
    private int pollId;
    private Set<Integer> options;

    public Poll(String name, Set<Integer> options) {
        this.name = name;
        this.options = options;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public Set<Integer> getOptions() {
        return options;
    }

    public void setOptions(Set<Integer> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        if (pollId != poll.pollId) return false;
        return options != null ? options.equals(poll.options) : poll.options == null;
    }

    @Override
    public int hashCode() {
        int result = pollId;
        result = 31 * result + (options != null ? options.hashCode() : 0);
        return result;
    }
}
