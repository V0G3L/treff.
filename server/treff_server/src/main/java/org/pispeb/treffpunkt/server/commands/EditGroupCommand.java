package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.descriptions.UsergroupEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import java.util.Date;

/**
 * a command to edit the name of a user group
 */
public class EditGroupCommand
        extends GroupCommand<EditGroupCommand.Input, EditGroupCommand.Output> {


    public EditGroupCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {
        // edit name
        usergroup.setName(input.group.name);

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        final UsergroupEditDescription group;

        public Input(Usergroup group, String token) {
            super(token, group.getId());
            this.group = new UsergroupEditDescription(group);
        }

        @Override
        public boolean syntaxCheck() {
            return validateGroupName(group.name);
        }
    }

    public static class Output extends CommandOutput { }
}
