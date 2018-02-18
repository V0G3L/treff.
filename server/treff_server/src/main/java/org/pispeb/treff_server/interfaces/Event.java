package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * An object representing a event with an title, position, start and end time
 * and a set of participants.
 */
public interface Event extends DataObject, Comparable<Event> {

    /**
     * Sets the title of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param title New title
     */
    void setTitle(String title);

    /**
     * Returns the title of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The title of this {@code Event}
     */
    String getTitle();

    /**
     * Sets the position of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position New position
     */
    void setPosition(Position position);

    /**
     * Returns the position of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The position of this {@code Event}
     */
    Position getPosition();

    /**
     * Sets the start time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeStart New start time
     */
    void setTimeStart(Date timeStart);

    /**
     * Returns the start time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The start time of this {@code Event}
     */
    Date getTimeStart();

    /**
     * Sets the end time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeEnd New end time
     */
    void setTimeEnd(Date timeEnd);

    /**
     * Returns the end time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The end time of this {@code Event}
     */
    Date getTimeEnd();

    /**
     * Returns the creator of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The creator of this {@code Event}
     */
    Account getCreator();

    /**
     * Adds a participant to this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    void addParticipant(Account participant);

    /**
     * Removes a participant from this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    void removeParticipant(Account participant);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that are participating in this {@code Event}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Account}s that are participating in this
     * {@code Event}.
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllParticipants();

}
