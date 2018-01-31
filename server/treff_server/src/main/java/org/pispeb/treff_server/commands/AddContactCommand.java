package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to add an Account to the contact-list of another Account
 */
public class AddContactCommand extends AbstractCommand {

    public AddContactCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        int actingAccountId = input.getActingAccount().getID();

        // determine locking order of the accounts
        Account actingAccount;
        Account newContact;
        if (actingAccountId < input.contactId) {
            // check if the acting account still exists
            actingAccount = getSafeForWriting(input.getActingAccount());
            if (actingAccount == null)
                return new ErrorOutput(ErrorCode.TOKENINVALID);

            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager
                    .getAccount(input.contactId));
            if (newContact == null)
                return new ErrorOutput(ErrorCode.USERIDINVALID);
        } else {
            // check if the new contact is valid
            newContact = getSafeForWriting(this.accountManager
                    .getAccount(input.contactId));
            if (newContact == null)
                return new ErrorOutput(ErrorCode.USERIDINVALID);

            // check if the acting account still exists
            actingAccount = getSafeForWriting(input.getActingAccount());
            if (actingAccount == null)
                return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // check block lists
        if (actingAccount.getAllBlocks().containsKey(input.contactId)) {
            return new ErrorOutput(ErrorCode.BLOCKINGALREADY);
        }
        if (newContact.getAllBlocks().containsKey(actingAccountId)) {
            return new ErrorOutput(ErrorCode.BEINGBLOCKED);
        }

        // execute the command
        // TODO friend request instead of direct add
        actingAccount.addContact(newContact);
        newContact.addContact(actingAccount);

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int contactId;

        public Input(@JsonProperty("id") int contactId,
                     @JsonProperty("token") String token) {
            super(token);
            this.contactId = contactId;
        }
    }

    public static class Output extends CommandOutput { }
}
