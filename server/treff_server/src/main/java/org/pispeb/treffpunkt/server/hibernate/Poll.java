package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Position;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * An object representing a poll with an question, a point in time at which
 * voting closes and poll options up for voting.
 */
@Entity
@Table(name = "polls")
public class Poll extends DataObject {

    @ManyToOne
    private Account creator;

    /**
         * Gets the title of this {@code Poll}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The question
         */
    public String getQuestion() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Sets the title of this {@code Poll}
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param question New question
         */
    public void setQuestion(String question) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    public Map<Integer, ? extends PollOption> getPollOptions() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Creates a new {@code PollOption} with the supplied details
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param position The position of the new {@code PollOption}
         * @param timeStart The start time of the new {@code PollOption}
         * @param timeEnd The end time of the new {@code PollOption}
         * @return The newly created {@code PollOption}
         */
    public PollOption addPollOption(Position position, Date timeStart, Date timeEnd) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Checks whether an {@code Account} can vote for multiple
         * {@code PollOption} at once in this {@code Poll}.
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return {@code true} if and only if voting for multiple
         * {@code PollOption}s is allowed
         */
    public boolean isMultiChoice() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Sets whether an {@code Account} can vote for multiple {@code PollOption}s
         * at once in this {@code Poll}.
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param multiChoice {@code true} if and only if voting for multiple
         *                    {@code PollOption}s is to be allowed
         */
    public void setMultiChoice(boolean multiChoice) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Gets the vote close time of this {@code Poll}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The vote close time
         */
    public Date getTimeVoteClose() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Sets the vote close time of this {@code Poll}
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param timeVoteClose The vote close time
         */
    public void setTimeVoteClose(Date timeVoteClose) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Gets the creator of this {@code Poll}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The creator
         */
    public Account getCreator() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    void setCreator(Account creator) {
        this.creator = creator;
    }

    /**
         * Ends the poll, locking the voting and creating an {@link Event} based
         * on the most popular option.
         * <p>
         * If there is a tie, the {@code PollOption} with the lowest ID is chosen.
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @return The created Event
         */
    public Event endPoll() {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
