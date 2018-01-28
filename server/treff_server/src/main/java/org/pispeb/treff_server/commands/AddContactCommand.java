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
        // get the account
        Account newContact = this.accountManager.getAccount(id);
        if (newContact == null) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        // get the locks
        if (actingAccount.compareTo(newContact) < 0) {
            actingAccount.getReadWriteLock().writeLock().lock();
            newContact.getReadWriteLock().writeLock().lock();
        } else {
            newContact.getReadWriteLock().writeLock().lock();
            actingAccount.getReadWriteLock().writeLock().lock();
        }
        try {
            // check if process is valid
            if (actingAccount.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (newContact.isDeleted()) {
                return new CommandResponse(StatusCode.USERIDINVALID);
            }
            if (actingAccount.getAllBlocks().containsKey(id)) {
                return new CommandResponse(StatusCode.BLOCKINGALREADY);
            }
            if (newContact.getAllBlocks().containsKey(actingAccount.getID())) {
                return new CommandResponse(StatusCode.BEINGBLOCKED);
            }
            // apply
            // TODO friend request instead of direct add
            actingAccount.addContact(newContact);
            newContact.addContact(actingAccount);
        } finally {
            actingAccount.getReadWriteLock().writeLock().unlock();
            newContact.getReadWriteLock().writeLock().unlock();
        }
        // respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
