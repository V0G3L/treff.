package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to add an Account to the contact-list of another Account
 */
public class AddContactCommand extends AbstractCommand {

    public AddContactCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
        throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int id = 0; //= input.getInt("id");
        int actingAccountID = 0; // TODO: migrate to new format

        // determine locking order of the accounts
        Account actingAccount;
        Account newContact;
        if (actingAccountID < id) {
            // check if the acting account still exists
            actingAccount = getSafeForWriting(this.accountManager
                    .getAccount(actingAccountID));
            if (actingAccount == null)
                return new ErrorOutput(ErrorCode.TOKENINVALID);

            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager.getAccount(id));
            if (newContact == null)
                return new ErrorOutput(ErrorCode.USERIDINVALID);
        } else {
            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager.getAccount(id));
            if (newContact == null)
                return new ErrorOutput(ErrorCode.USERIDINVALID);

            // check if the acting account still exists
            actingAccount = getSafeForWriting(this.accountManager
                    .getAccount(actingAccountID));
            if (actingAccount == null)
                return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // check block lists
        if (actingAccount.getAllBlocks().containsKey(id)) {
            return new ErrorOutput(ErrorCode.BLOCKINGALREADY);
        }
        if (newContact.getAllBlocks().containsKey(actingAccountID)) {
            return new ErrorOutput(ErrorCode.BEINGBLOCKED);
        }

        // execute the command
        // TODO friend request instead of direct add
        actingAccount.addContact(newContact);
        newContact.addContact(actingAccount);

        // respond
        throw new UnsupportedOperationException();
    }

}
