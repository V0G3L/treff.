package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class UpdateSQL extends SQLObject implements Update {

    private static final TableName TABLE_NAME = TableName.UPDATES;

    UpdateSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public Date getTime()  {
        return null;
    }

    @Override
    public UpdateType getType()  {
        return null;
    }

    @Override
    public JsonObject getUpdate()  {
        // read from DB, assemble JsonObject
        return null;
    }

    @Override
    public void addAffectedAccount(Account account)  {
        account.addUpdate(this);
    }

    @Override
    public boolean removeAffectedAccount(Account account)
             {
        // remove user from set
        // if set is empty after removal, delete update from db
        //      and return true
        //      Anything holding a reference to this should let go
        //      of it when this returns true
        return false;
    }

    @Override
    public Map<Integer, Account> getAffectedAccounts()  {
        return null;
    }

    @Override
    public int compareTo(Update o) {
        // oldest to newest
        return 0;
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

}
