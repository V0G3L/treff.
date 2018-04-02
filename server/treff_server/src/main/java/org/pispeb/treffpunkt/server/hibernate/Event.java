package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An object representing a event with an title, position, start and end time
 * and a set of participants.
 */
@Entity
@Table(name = "events")
public class Event extends DataObject {

    @Column
    private String title;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private Date timeStart;
    @Column
    private Date timeEnd;

    @ManyToOne
    private Account creator;

    @ManyToMany
    @JoinTable(name = "event_participants")
    private Set<Account> participants = new HashSet<>();

    public Event() { }

    Event(String title, Position position, Date timeStart, Date timeEnd, Account creator) {
        this.title = title;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.creator = creator;
    }

    /**
     * Sets the title of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param title New title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The title of this {@code Event}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the position of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position New position
     */
    public void setPosition(Position position) {
        this.latitude = position.latitude;
        this.longitude = position.longitude;
    }

    /**
     * Returns the position of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The position of this {@code Event}
     */
    public Position getPosition() {
        return new Position(latitude, longitude);
    }

    /**
     * Sets the start time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeStart New start time
     */
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * Returns the start time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The start time of this {@code Event}
     */
    public Date getTimeStart() {
        return timeStart;
    }

    /**
     * Sets the end time of this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeEnd New end time
     */
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * Returns the end time of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The end time of this {@code Event}
     */
    public Date getTimeEnd() {
        return timeEnd;
    }

    /**
     * Returns the creator of this {@code Event}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The creator of this {@code Event}
     */
    public Account getCreator() {
        return creator;
    }

    /**
     * Adds a participant to this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    public void addParticipant(Account participant) {
        participants.add(participant);
    }

    /**
     * Removes a participant from this {@code Event}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param participant The participant
     */
    public void removeParticipant(Account participant) {
        participants.remove(participant);
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
        return toMap(participants);
    }
}
