package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.UsergroupEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to edit the name of a user group
 */
public class EditGroupCommand extends GroupCommand {


    public EditGroupCommand(AccountManager accountManager,
                            ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
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
            super(token, group.id, new int[0]);
            this.group = group;
        }

        @Override
        public boolean syntaxCheck() {
            return validateGroupName(group.name);
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }
}
