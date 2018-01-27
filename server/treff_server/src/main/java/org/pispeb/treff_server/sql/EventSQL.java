package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

public class EventSQL extends SQLObject implements Event {

    private static final TableName TABLE_NAME = TableName.EVENTS;

    EventSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public void setTitle(String title)  {

    }

    @Override
    public String getTitle()  {
        return null;
    }

    @Override
    public void setPosition(Position position)  {

    }

    @Override
    public Position getPosition()  {
        return null;
    }

    @Override
    public void setTimeStart()  {

    }

    @Override
    public Date getTimeStart()  {
        return null;
    }

    @Override
    public void setTimeEnd()  {

    }

    @Override
    public Date getTimeEnd()  {
        return null;
    }

    @Override
    public Date getTimeCreated()  {
        return null;
    }

    @Override
    public Account getCreator()  {
        return null;
    }

    @Override
    public void addParticipant(Account participant)  {

    }

    @Override
    public void removeParticipant(Account participant)
             {

    }

    @Override
    public Set<Account> getAllParticipants()  {
        return null;
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
