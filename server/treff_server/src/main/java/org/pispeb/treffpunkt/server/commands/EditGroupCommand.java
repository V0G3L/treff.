package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.UsergroupEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit the name of a user group
 */
public class EditGroupCommand extends GroupCommand {


    public EditGroupCommand(SessionFactory sessionFactory,
                            ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                Permission.EDIT_GROUP,
                ErrorCode.NOPERMISSIONEDITGROUP);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        // edit name
        Input input = (Input) commandInput;
        usergroup.setName(input.group.name);

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupInput {

        final UsergroupEditDescription group;

        public Input(@JsonProperty("group") UsergroupEditDescription group,
                     @JsonProperty("token") String token) {
            super(token, group.id);
            this.group = group;
        }

        @Override
        public boolean syntaxCheck() {
            return validateGroupName(group.name);
        }
    }

    public static class Output extends CommandOutput { }
}
