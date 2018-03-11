package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * An object representing a poll option with a position, a start and end time
 * which voting closes and poll options up for voting.
 */
public interface PollOption extends DataObject, Comparable<PollOption> {

    /**
     * Sets the position of this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position New position
     */
    void setPosition(Position position);

    /**
     * Returns the position of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The position of this {@code PollOption}
     */
    Position getPosition();

    /**
     * Sets the start time of this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeStart New start time
     */
    void setTimeStart(Date timeStart);

    /**
     * Returns the start time of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The start time of this {@code PollOption}
     */
    Date getTimeStart();

    /**
     * Sets the end time of this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeEnd New end time
     */
    void setTimeEnd(Date timeEnd);

    /**
     * Returns the end time of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The end time of this {@code PollOption}
     */
    Date getTimeEnd();

    /**
     * Adds a voter to this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    void addVoter(Account voter);

    /**
     * Removes a voter from this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    void removeVoter(Account voter);

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
    Map<Integer, ? extends Account> getVoters();

}
