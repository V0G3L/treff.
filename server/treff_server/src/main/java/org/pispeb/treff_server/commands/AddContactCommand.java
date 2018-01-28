package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;

// TODO needs to be tested

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
                                              int actingAccountID) {
        int id = input.getInt("id");

        // determine locking order of the accounts
        Account actingAccount;
        Account newContact;
        if (actingAccountID < id) {
            // check if the acting account still exists
            actingAccount = getSafeForWriting(this.accountManager
                    .getAccount(actingAccountID));
            if (actingAccount == null)
                return new CommandResponse(StatusCode.TOKENINVALID);

            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager.getAccount(id));
            if (newContact == null)
                return new CommandResponse((StatusCode.USERIDINVALID));
        } else {
            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager.getAccount(id));
            if (newContact == null)
                return new CommandResponse((StatusCode.USERIDINVALID));

            // check if the acting account still exists
            actingAccount = getSafeForWriting(this.accountManager
                    .getAccount(actingAccountID));
            if (actingAccount == null)
                return new CommandResponse(StatusCode.TOKENINVALID);
        }

        // check block lists
        if (actingAccount.getAllBlocks().containsKey(id)) {
            return new CommandResponse(StatusCode.BLOCKINGALREADY);
        }
        if (newContact.getAllBlocks().containsKey(actingAccountID)) {
            return new CommandResponse(StatusCode.BEINGBLOCKED);
        }

        // execute the command
        // TODO friend request instead of direct add
        actingAccount.addContact(newContact);
        newContact.addContact(actingAccount);

        // respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
