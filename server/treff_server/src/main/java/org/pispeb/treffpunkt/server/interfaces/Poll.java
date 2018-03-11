package org.pispeb.treffpunkt.server.interfaces;

import org.pispeb.treffpunkt.server.Position;

import java.util.Date;
import java.util.Map;

/**
 * An object representing a poll with an question, a point in time at which
 * voting closes and poll options up for voting.
 */
public interface Poll extends DataObject, Comparable<Poll> {

    /**
     * Gets the title of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The question
     */
    String getQuestion();

    /**
     * Sets the title of this {@code Poll}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param question New question
     */
    void setQuestion(String question);

    Map<Integer, ? extends PollOption> getPollOptions();

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
    PollOption addPollOption(Position position, Date timeStart, Date timeEnd);

    /**
     * Checks whether an {@code Account} can vote for multiple
     * {@code PollOption} at once in this {@code Poll}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return {@code true} if and only if voting for multiple
     * {@code PollOption}s is allowed
     */
    boolean isMultiChoice();

    /**
     * Sets whether an {@code Account} can vote for multiple {@code PollOption}s
     * at once in this {@code Poll}.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param multiChoice {@code true} if and only if voting for multiple
     *                    {@code PollOption}s is to be allowed
     */
    void setMultiChoice(boolean multiChoice);

    /**
     * Gets the vote close time of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The vote close time
     */
    Date getTimeVoteClose();

    /**
     * Sets the vote close time of this {@code Poll}
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param timeVoteClose The vote close time
     */
    void setTimeVoteClose(Date timeVoteClose);

    /**
     * Gets the creator of this {@code Poll}
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The creator
     */
    Account getCreator();

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
    Event endPoll();
}
