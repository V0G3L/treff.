package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.List;

public interface Poll {

    String getQuestion() throws DatabaseException;

    void setQuestion(String question) throws DatabaseException;

    List<PollOption> getPollOptions() throws DatabaseException;

    PollOption addPollOption(String title, Position position) throws
            DatabaseException;

    /**
     * Removes the supplied {@link PollOption} from the poll.
     *
     * @return <code>true</code> if the {@link Poll} previously contained
     * the supplied {@link PollOption} and it was removed, <code>false</code>
     * if the supplied {@link PollOption} was not part of this {@link Poll}
     * anyways.
     */
    // TODO: phrasing
    boolean removePollOption(PollOption pollOption) throws DatabaseException;

    boolean isMultiChoice() throws DatabaseException;

    void setMultiChoice(boolean multiChoice) throws DatabaseException;

    /**
     * Ends the poll, locking the voting and creating an {@link Event} based
     * on the most popular option.
     *
     * @return The created Event
     */
    Event endPoll() throws DatabaseException;

    /**
     * Cancels the poll. Like {@link #endPoll()} but doesn't create an
     * {@link Event}.
     */
    void cancelPoll() throws DatabaseException;
}
