package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.DataObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * a command to get all user groups of an account
 */
public class ListGroupsCommand extends AbstractCommand
        <ListGroupsCommand.Input,ListGroupsCommand.Output> {

    public ListGroupsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) throws
            DatabaseException {

        Account actingAccount = input.getActingAccount();

        // get groups
        List<Integer> outputArray = actingAccount
                .getAllGroups()
                .values()
                .stream()
                .map(DataObject::getID)
                .collect(Collectors.toList());
        return new Output(outputArray);
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        public final List<Integer> groupIDs;

        public Output(List<Integer> groupIDs) {
            this.groupIDs = groupIDs;
        }
    }
}
