package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Update;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Set;

public class UpdateSQL implements Update {

    @Override
    public Date getTime() {
        return null;
    }

    @Override
    public UpdateType getType() {
        return null;
    }

    @Override
    public JsonObject getUpdate() {
        // read from DB, assemble JsonObject
        return null;
    }

    @Override
    public void addAffectedAccount(Account account) {
    }

    @Override
    public boolean removeAffectedAccount(Account account) {
        // remove user from set
        // if set is empty after removal, delete update from db
        //      and return true
        //      Anything holding a reference to this should let go
        //      of it when this returns true
        return false;
    }

    @Override
    public Set<Account> getAffectedAccounts() {
        return null;
    }

    @Override
    public int compareTo(Update o) {
        // oldest to newest
        return 0;
    }
}
