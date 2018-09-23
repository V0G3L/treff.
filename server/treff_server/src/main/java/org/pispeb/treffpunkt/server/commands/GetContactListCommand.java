package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.service.domain.ContactList;

/**
 * a command to get a list of all contacts of the executing account
 */
public class GetContactListCommand extends AbstractCommand
        <GetContactListCommand.Input,GetContactListCommand.Output> {


    public GetContactListCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        // check if account still exists
        Account actingAccount = input.getActingAccount();

        return new Output(actingAccount.getAllContacts().keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .toArray(),
                actingAccount.getAllIncomingContactRequests().keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray(),
                actingAccount.getAllOutgoingContactRequests().keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray(),
                actingAccount.getAllBlocks().keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray());
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        public ContactList contactList;

        Output(int[] contacts, int[] incoming, int[] outgoing, int[] blocks) {
            this.contactList = new ContactList(contacts, incoming, outgoing, blocks);
        }
    }

}
