package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.PollOption;

import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;

public class PollOptionSQL extends SQLObject implements PollOption {

    PollOptionSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config);
    }

    @Override
    public String getTitle() throws DatabaseException {
        return null;
    }

    @Override
    public void setTitle(String title) throws DatabaseException {

    }

    @Override
    public Position getPosition() throws DatabaseException {
        return null;
    }

    @Override
    public void setPosition(Position position) throws DatabaseException {

    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
