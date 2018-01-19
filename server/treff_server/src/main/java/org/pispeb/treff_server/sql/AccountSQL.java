package org.pispeb.treff_server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.interfaces.Update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

public class AccountSQL extends SQLObject implements Account {

    // TODO: synchronized setters everywhere

    AccountSQL(int id, MysqlDataSource dataSource) {
        super(id, dataSource);
    }


    @Override
    public String getUsername() {
        // TODO: write SQL statements
        return null;
    }

    @Override
    public void setUsername(String username) throws
            DuplicateUsernameException {
    }

    @Override
    public boolean checkPassword(String password) {
        // get salt from db
        // hash supplied pw with salt
        // compare with hash in db
        return false;
    }

    @Override
    public void setPassword(String password) {
        // get salt from db
        // hash supplied pw with salt
        // store hash in db
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException {

    }

    @Override
    public Map<Integer, Usergroup> getAllGroups() {
        return null;
    }

    @Override
    public void addToGroup(Usergroup usergroup) {

    }

    @Override
    public void removeFromGroup(Usergroup usergroup) {

    }

    @Override
    public Position getLastPosition() {
        return null;
    }

    @Override
    public Date getLastPositionTime() {
        return null;
    }

    @Override
    public void updatePosition(Position position) {
        // not persistent
    }

    @Override
    public void addUpdate(Update update) {

    }

    @Override
    public SortedSet<Update> getUndeliveredUpdates() {
        return null;
    }

    @Override
    public void markUpdateAsDelivered(Update update) {

    }

    @Override
    public void addUpdateListener(AccountUpdateListener updateListener) {

    }

    @Override
    public void removeUpdateListener(AccountUpdateListener updateListener) {

    }

    @Override
    public void delete() {
        // removes itself from all groups
        // removes all contacts (will also removed them from the other sides)
        // clears its own blocklist
        // clears itself from all other blocklists (somehow, unclear)
        // events and polls have to be able to handle non-existent creators
    }
}
