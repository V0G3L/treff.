package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Update;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class UpdateSQL extends SQLObject implements Update {

    UpdateSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config);
    }

    @Override
    public Date getTime() throws DatabaseException {
        return null;
    }

    @Override
    public UpdateType getType() throws DatabaseException {
        return null;
    }

    @Override
    public JsonObject getUpdate() throws DatabaseException {
        // read from DB, assemble JsonObject
        return null;
    }

    @Override
    public void addAffectedAccount(Account account) throws DatabaseException {
    }

    @Override
    public boolean removeAffectedAccount(Account account)
            throws DatabaseException {
        // remove user from set
        // if set is empty after removal, delete update from db
        //      and return true
        //      Anything holding a reference to this should let go
        //      of it when this returns true
        return false;
    }

    @Override
    public Set<Account> getAffectedAccounts() throws DatabaseException {
        return null;
    }

    @Override
    public int compareTo(Update o) {
        // oldest to newest
        return 0;
    }
}
