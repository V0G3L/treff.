package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;

public class PollSQL extends SQLObject implements Poll {


    private static final TableName TABLE_NAME = TableName.POLLS;

    PollSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public String getQuestion()  {
        return null;
    }

    @Override
    public void setQuestion(String question)  {

    }

    @Override
    public List<PollOption> getPollOptions()  {
        return null;
    }

    @Override
    public PollOption addPollOption(String title, Position position)
             {
        return null;
    }

    @Override
    public boolean removePollOption(PollOption pollOption)
             {
        return false;
    }

    @Override
    public boolean isMultiChoice()  {
        return false;
    }

    @Override
    public void setMultiChoice(boolean multiChoice)  {

    }

    @Override
    public Event endPoll()  {
        return null;
    }

    @Override
    public void cancelPoll()  {

    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override
    public int getID() {
        return id;
    }
}
