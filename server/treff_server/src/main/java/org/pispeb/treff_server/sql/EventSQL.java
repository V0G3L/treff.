package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;

import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class EventSQL extends SQLObject implements Event {

    EventSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config);
    }

    @Override
    public void setTitle(String title) throws DatabaseException {

    }

    @Override
    public String getTitle() throws DatabaseException {
        return null;
    }

    @Override
    public void setPosition(Position position) throws DatabaseException {

    }

    @Override
    public Position getPosition() throws DatabaseException {
        return null;
    }

    @Override
    public void setTimeStart() throws DatabaseException {

    }

    @Override
    public Date getTimeStart() throws DatabaseException {
        return null;
    }

    @Override
    public void setTimeEnd() throws DatabaseException {

    }

    @Override
    public Date getTimeEnd() throws DatabaseException {
        return null;
    }

    @Override
    public Date getTimeCreated() throws DatabaseException {
        return null;
    }

    @Override
    public Account getCreator() throws DatabaseException {
        return null;
    }

    @Override
    public void addParticipant(Account participant) throws DatabaseException {

    }

    @Override
    public void removeParticipant(Account participant)
            throws DatabaseException {

    }

    @Override
    public Set<Account> getAllParticipants() throws DatabaseException {
        return null;
    }
}
