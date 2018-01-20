package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.interfaces.Update;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;

public class AccountSQL extends SQLObject implements Account {

    private static final Object usernameLock = new Object();

    AccountSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config);
    }

    // TODO: synchronized setters everywhere

    @Override
    public String getUsername() throws DatabaseException {
        // TODO: write SQL statements
        try {
            return (String) database.getQueryRunner().query(
                    "SELECT username FROM accounts WHERE id=?;",
                    new MapHandler(),
                    id
            ).get("username");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setUsername(String username) throws
            DuplicateUsernameException, DatabaseException {
        // process username changes of everyone in sequence to ensure
        // uniqueness
        synchronized (usernameLock) {
            // check for duplicates
            try {
                if (database.getQueryRunner().query(
                        "SELECT id FROM accounts WHERE username=?;",
                        new DuplicateCheckHandler(),
                        username)) {
                    throw new DuplicateUsernameException();
                } else {
                    database.getQueryRunner().update(
                            "UPDATE accounts set username=? WHERE id=?;",
                            username,
                            id);
                }
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    @Override
    public boolean checkPassword(String password) throws DatabaseException {

        return false;
    }

    @Override
    public void setPassword(String password) throws DatabaseException {
        // get salt from db
        // hash supplied pw with salt
        // store hash in db
    }

    @Override
    public String getEmail() throws DatabaseException {
        return null;
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException,
            DatabaseException {

    }

    @Override
    public Map<Integer, Usergroup> getAllGroups() throws DatabaseException {
        return null;
    }

    @Override
    public void addToGroup(Usergroup usergroup) throws DatabaseException {

    }

    @Override
    public void removeFromGroup(Usergroup usergroup) throws DatabaseException {

    }

    @Override
    public Position getLastPosition() throws DatabaseException {
        return null;
    }

    @Override
    public Date getLastPositionTime() throws DatabaseException {
        return null;
    }

    @Override
    public void updatePosition(Position position) throws DatabaseException {
        // not persistent
    }

    @Override
    public void addUpdate(Update update) throws DatabaseException {

    }

    @Override
    public SortedSet<Update> getUndeliveredUpdates() throws DatabaseException {
        return null;
    }

    @Override
    public void markUpdateAsDelivered(Update update) throws DatabaseException {

    }

    @Override
    public void addUpdateListener(AccountUpdateListener updateListener)
            throws DatabaseException {

    }

    @Override
    public void removeUpdateListener(AccountUpdateListener updateListener)
            throws DatabaseException {

    }

    @Override
    public void delete() throws DatabaseException {
        // removes itself from all groups
        // removes all contacts (will also removed them from the other sides)
        // clears its own blocklist
        // clears itself from all other blocklists (somehow, unclear)
        // events and polls have to be able to handle non-existent creators
    }
}
