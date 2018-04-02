package org.pispeb.treffpunkt.server.hibernate;

import org.hibernate.Session;
import org.pispeb.treffpunkt.server.Position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An object representing a poll with an question, a point in time at which
 * voting closes and poll options up for voting.
 */
@Entity
@Table(name = "polls")
public class Poll extends DataObject {

    @Column
    private String question;
    @Column
    private boolean isMultiChoice;
    @Column
    private Date timeVoteClose;

    @ManyToOne
    private Account creator;

    @OneToMany
    private Set<PollOption> options = new HashSet<>();

    public Poll() { }

    Poll(String question, boolean isMultiChoice, Date timeVoteClose, Account creator) {
        this.question = question;
        this.isMultiChoice = isMultiChoice;
        this.timeVoteClose = timeVoteClose;
        this.creator = creator;
    }

    /**
     * Gets the title of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the title of this {@code Poll}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param question New question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<Integer, ? extends PollOption> getPollOptions() {
        return toMap(options);
    }

    /**
     * Creates a new {@code PollOption} with the supplied details
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position  The position of the new {@code PollOption}
     * @param timeStart The start time of the new {@code PollOption}
     * @param timeEnd   The end time of the new {@code PollOption}
     * @return The newly created {@code PollOption}
     */
    public PollOption addPollOption(Position position, Date timeStart, Date timeEnd,
                                    Session session) {
        PollOption option = new PollOption(position, timeStart, timeEnd);
        session.save(option);
        options.add(option);
        return option;
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
        return isMultiChoice;
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
        this.isMultiChoice = multiChoice;
    }

    /**
     * Gets the vote close time of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The vote close time
     */
    public Date getTimeVoteClose() {
        return timeVoteClose;
    }

    /**
     * Sets the vote close time of this {@code Poll}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeVoteClose The vote close time
     */
    public void setTimeVoteClose(Date timeVoteClose) {
        this.timeVoteClose = timeVoteClose;
    }

    /**
     * Gets the creator of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The creator
     */
    public Account getCreator() {
        return creator;
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
