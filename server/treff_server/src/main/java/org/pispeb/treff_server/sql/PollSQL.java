package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Event;
import org.pispeb.treff_server.Poll;
import org.pispeb.treff_server.PollOption;
import org.pispeb.treff_server.Position;

import java.util.List;

public class PollSQL implements Poll {
    @Override
    public String getQuestion() {
        return null;
    }

    @Override
    public void setQuestion(String question) {

    }

    @Override
    public List<PollOption> getPollOptions() {
        return null;
    }

    @Override
    public PollOption addPollOption(String title, Position position) {
        return null;
    }

    @Override
    public boolean removePollOption(PollOption pollOption) {
        return false;
    }

    @Override
    public boolean isMultiChoice() {
        return false;
    }

    @Override
    public void setMultiChoice(boolean multiChoice) {

    }

    @Override
    public Event endPoll() {
        return null;
    }

    @Override
    public void cancelPoll() {

    }
}
