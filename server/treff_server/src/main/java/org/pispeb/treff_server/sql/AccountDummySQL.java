package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.sql.interfaces.Account;

public class AccountDummySQL implements Account {
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

    @Override
    public void delete() {

    }
}
