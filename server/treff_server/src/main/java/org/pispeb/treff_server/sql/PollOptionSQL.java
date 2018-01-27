package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;

public class PollOptionSQL extends SQLObject implements PollOption {

    private static final TableName TABLE_NAME = TableName.POLLOPTIONS;

    PollOptionSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public String getTitle()  {
        return null;
    }

    @Override
    public void setTitle(String title)  {

    }

    @Override
    public Position getPosition()  {
        return null;
    }

    @Override
    public void setPosition(Position position)  {

    }

    @Override
    public void addVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void removeVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, Account> getVoters() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(PollOption o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
