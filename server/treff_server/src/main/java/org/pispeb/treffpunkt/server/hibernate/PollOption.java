package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An object representing a poll option with a position, a start and end time
 * which voting closes and poll options up for voting.
 */
@Entity
@Table(name = "polloptions")
public class PollOption extends DataObject {

    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private Date timeStart;
    @Column
    private Date timeEnd;

    @ManyToMany
    private Set<Account> voters = new HashSet<>();

    public PollOption() {
    }

    PollOption(Position position, Date timeStart, Date timeEnd) {
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    /**
     * Sets the position of this {@code PollOption}
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
     * Returns the position of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The position of this {@code PollOption}
     */
    public Position getPosition() {
        return new Position(latitude, longitude);
    }

    /**
     * Sets the start time of this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeStart New start time
     */
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * Returns the start time of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The start time of this {@code PollOption}
     */
    public Date getTimeStart() {
        return timeStart;
    }

    /**
     * Sets the end time of this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeEnd New end time
     */
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * Returns the end time of this {@code PollOption}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The end time of this {@code PollOption}
     */
    public Date getTimeEnd() {
        return timeEnd;
    }

    /**
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
        return toMap(voters);
    }

    /**
     * Adds a voter to this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    public void addVoter(Account voter) {
        voters.add(voter);
    }

    /**
     * Removes a voter from this {@code PollOption}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param voter The voter
     */
    public void removeVoter(Account voter) {
        voters.remove(voter);
    }

}
