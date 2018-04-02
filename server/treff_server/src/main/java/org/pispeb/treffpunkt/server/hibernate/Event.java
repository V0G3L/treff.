package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Position;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * An object representing a event with an title, position, start and end time
 * and a set of participants.
 */
@Entity
@Table(name = "events")
public class Event extends DataObject {

    @ManyToOne
    private Account creator;

    /**
     * Sets the title of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param title New title
     */
    public void setTitle(String title) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the title of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The title of this {@code Event}
     */
    public String getTitle() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Sets the position of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position New position
     */
    public void setPosition(Position position) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the position of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The position of this {@code Event}
     */
    public Position getPosition() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Sets the start time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeStart New start time
     */
    public void setTimeStart(Date timeStart) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the start time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The start time of this {@code Event}
     */
    public Date getTimeStart() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Sets the end time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeEnd New end time
     */
    public void setTimeEnd(Date timeEnd) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the end time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The end time of this {@code Event}
     */
    public Date getTimeEnd() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the creator of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The creator of this {@code Event}
     */
    public Account getCreator() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    void setCreator(Account creator) {
        this.creator = creator;
    }

    /**
     * Adds a participant to this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    public void addParticipant(Account participant) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Removes a participant from this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    public void removeParticipant(Account participant) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

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
    public Map<Integer, ? extends Account> getAllParticipants() {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
