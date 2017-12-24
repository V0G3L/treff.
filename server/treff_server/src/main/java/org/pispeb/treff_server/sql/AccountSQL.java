package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Account;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

public class AccountSQL implements Account {

    private SQLDatabase sqlDatabase;
    private boolean valid;
    private int id;
    private Position lastPosition;

    AccountSQL(int id) {
        this.id = id;
    }

    void invalidate() {
        this.valid = false;
    }

    // TODO: add throws for AccountInvalidException

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void setUsername(String username) throws DuplicateUsernameException {

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
    public Position getLastPosition() {
        return null;
    }

    @Override
    public void updatePosition(Position position) {
        // not persistent
    }

    @Override
    public void delete() {

    }
}
