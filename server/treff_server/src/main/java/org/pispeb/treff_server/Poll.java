package org.pispeb.treff_server;

import java.util.List;

public interface Poll {

    String getQuestion();
    void setQuestion(String question);

    List<PollOption> getPollOptions();
    PollOption addPollOption(String title, Position position);

    /**
     * Removes the supplied {@link PollOption} from the poll.
     * @return <code>true</code> if the {@link Poll} previously contained
     * the supplied {@link PollOption} and it was removed, <code>false</code>
     * if the supplied {@link PollOption} was not part of this {@link Poll}
     * anyways.
     */
    // TODO: phrasing
    boolean removePollOption(PollOption pollOption);

    boolean isMultiChoice();
    void setMultiChoice(boolean multiChoice);
}
