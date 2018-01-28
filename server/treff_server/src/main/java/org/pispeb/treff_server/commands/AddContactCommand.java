package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.concurrent.locks.Lock;

/**
 * a command to add an Account to the contact-list of another Account
 */
public class AddContactCommand extends AbstractCommand {

    public AddContactCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("id", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              Account actingAccount) {
        int id = input.getInt("id");
        // get the accounts
        Account ownAccount = this.accountManager.getAccountByLoginToken(input
                .getString("token"));
        if (ownAccount == null) {
            return new CommandResponse(StatusCode.TOKENINVALID);
        }
        Account newContact = this.accountManager.getAccount(id);
        if (newContact== null) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        // check block-lists and apply changes
        Lock ownLock = ownAccount.getReadWriteLock().readLock();
        Lock contactLock = newContact.getReadWriteLock().readLock();
        if (ownAccount.compareTo(newContact) < 0) {
            ownLock.lock();
            contactLock.lock();
        } else {
            contactLock.lock();
            ownLock.lock();
        }
        try {
            if (ownAccount.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (newContact.isDeleted()) {
                return new CommandResponse(StatusCode.USERIDINVALID);
            }
            if (ownAccount.getAllBlocks().containsKey(id)) {
                return new CommandResponse(StatusCode.BEINGBLOCKED);
            }
            if (newContact.getAllBlocks().containsKey(ownAccount.getID())) {
                return new CommandResponse(StatusCode.BLOCKINGALREADY);
            }
            // TODO friend request instead of direct add
            ownAccount.addContact(newContact);
            newContact.addContact(ownAccount);
        } finally {
            ownLock.unlock();
            contactLock.unlock();
        }
        // respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
