package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Position;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An object representing a poll option with a position, a start and end time
 * which voting closes and poll options up for voting.
 */
public class PollOption extends DataObject {
    /**
         * Sets the position of this {@code PollOption}
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param position New position
         */
    public void setPosition(Position position) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Returns the position of this {@code PollOption}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The position of this {@code PollOption}
         */
    public Position getPosition() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Sets the start time of this {@code PollOption}
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param timeStart New start time
         */
    public void setTimeStart(Date timeStart) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Returns the start time of this {@code PollOption}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The start time of this {@code PollOption}
         */
    public Date getTimeStart() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Sets the end time of this {@code PollOption}
         * <p>
         * Requires a {@code WriteLock}.
         *
         * @param timeEnd New end time
         */
    public void setTimeEnd(Date timeEnd) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Returns the end time of this {@code PollOption}
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The end time of this {@code PollOption}
         */
    public Date getTimeEnd() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         *
         * Returns an unmodifiable [ID -> {@code Account}] map holding all
         * {@code Account}s that are voting for this {@code PollOption}.
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return Map of {@code Account}s that are voting for this
         * {@code PollOption}.
         * @see java.util.Collections#unmodifiableMap(Map)
         */
    public Map<Integer, ? extends Account> getVoters() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Adds a voter to this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    public void addVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Removes a voter from this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    public void removeVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

}
