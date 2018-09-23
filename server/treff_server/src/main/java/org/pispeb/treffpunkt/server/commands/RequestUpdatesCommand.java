package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Update;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * a command to request updates from the server
 */
public class RequestUpdatesCommand extends AbstractCommand
        <RequestUpdatesCommand.Input,RequestUpdatesCommand.Output> {


    public RequestUpdatesCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        // check if account still exists
        Account actingAccount = input.getActingAccount();
        if (actingAccount == null)
            throw ErrorCode.TOKENINVALID.toWebException();

        // get the Updates
        SortedSet<Update> updates = actingAccount.getUndeliveredUpdates();
        // LinkedHashSet to preserve insertion order
        Set<String> updatecontents = new LinkedHashSet<>();
        for (Update u : updates) {
            updatecontents.add(u.getUpdate());
            actingAccount.markUpdateAsDelivered(u);
        }

        return new Output(new ArrayList<>(updatecontents));
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        public final List<String> updates;

        public Output(List<String> updates) {
            this.updates = updates;
        }
    }
}
