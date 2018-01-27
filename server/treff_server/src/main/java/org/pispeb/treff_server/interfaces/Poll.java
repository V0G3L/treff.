package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Poll extends DataObject, Comparable<Poll> {

    String getQuestion();

    void setQuestion(String question);

    Map<Integer, PollOption> getPollOptions();

    PollOption addPollOption(Position position, Date timeStart, Date timeEnd);

    boolean isMultiChoice();

    void setMultiChoice(boolean multiChoice);

    Date getTimeVoteClose();

    void setTimeVoteClose(Date timeVoteClose);

    Account getCreator();

    /**
     * Ends the poll, locking the voting and creating an {@link Event} based
     * on the most popular option.
     *
     * @return The created Event
     */
    Event endPoll();
}
