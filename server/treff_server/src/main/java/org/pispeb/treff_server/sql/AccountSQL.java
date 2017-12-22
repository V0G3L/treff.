package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Account;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

public class AccountSQL implements Account {

    private SQLDatabase sqlDatabase;
    private boolean valid;
    private int id;

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
        return false;
    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException {

    }
}
