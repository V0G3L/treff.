package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.descriptions.UsergroupCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to create a user group
 */
public class CreateGroupCommand extends AbstractCommand
        <CreateGroupCommand.Input,CreateGroupCommand.Output> {


    public CreateGroupCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {
                Account actingAccount = input.getActingAccount();

        // check that all IDs are valid and remove duplicates
        Set<Account> members = new HashSet<>();
        for (int memberId : input.group.memberIDs) {
            Account currentAccount = accountManager.getAccount(memberId);
            if (currentAccount == null) {
                throw ErrorCode.USERIDINVALID.toWebException();
            }
            members.add(currentAccount);
        }

        // TODO: check if all other members are in contacts

        // create the group
        Usergroup usergroup = actingAccount.createGroup(input.group.name, members, session);

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);
        HashSet<Account> affected =
                new HashSet<>(usergroup.getAllMembers().values());
        affected.remove(input.getActingAccount());
        accountManager.createUpdate(update, affected);

        // respond
        return new Output(usergroup.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final UsergroupCreateDescription group;

        public Input(org.pispeb.treffpunkt.server.service.domain.Usergroup group, String token) {
            super(token);
            this.group = new UsergroupCreateDescription(group);
        }

        @Override
        public boolean syntaxCheck() {
            return validateGroupName(group.name);
        }
    }

    public static class Output extends CommandOutput {

        public final int groupId;

        Output(int groupId) {
            this.groupId = groupId;
        }
    }
}
