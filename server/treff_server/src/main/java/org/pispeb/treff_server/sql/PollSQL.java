package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import java.util.List;
import java.util.Properties;

public class PollSQL extends SQLObject implements Poll {

    PollSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config);
    }

    @Override
    public String getQuestion() throws DatabaseException {
        return null;
    }

    @Override
    public void setQuestion(String question) throws DatabaseException {

    }

    @Override
    public List<PollOption> getPollOptions() throws DatabaseException {
        return null;
    }

    @Override
    public PollOption addPollOption(String title, Position position)
            throws DatabaseException {
        return null;
    }

    @Override
    public boolean removePollOption(PollOption pollOption)
            throws DatabaseException {
        return false;
    }

    @Override
    public boolean isMultiChoice() throws DatabaseException {
        return false;
    }

    @Override
    public void setMultiChoice(boolean multiChoice) throws DatabaseException {

    }

    @Override
    public Event endPoll() throws DatabaseException {
        return null;
    }

    @Override
    public void cancelPoll() throws DatabaseException {

    }
}
