package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.service.api.AccountAPI;
import org.pispeb.treffpunkt.server.service.domain.Account;

public class AccountImpl extends ServiceImpl implements AccountAPI {
    @Override
    public Account getDetails(int aid) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int getID(String name) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setUsername(String username, String password) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setEmail(String email, String password) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setPassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
