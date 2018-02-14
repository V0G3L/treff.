package org.pispeb.treff_server.commands.descriptions;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import java.util.Date;
import java.util.Map;

/**
 * @author tim
 */
public class PollComplete extends Description implements Poll {

    @Override
    public String getQuestion() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setQuestion(String question) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, PollOption> getPollOptions() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public PollOption addPollOption(Position position, Date timeStart, Date
            timeEnd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMultiChoice() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setMultiChoice(boolean multiChoice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getTimeVoteClose() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTimeVoteClose(Date timeVoteClose) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Account getCreator() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Event endPoll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(Poll o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
