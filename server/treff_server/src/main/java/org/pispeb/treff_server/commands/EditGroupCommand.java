package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.UsergroupEditDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to edit the name of a Usergroup
 */
public class EditGroupCommand extends AbstractCommand {

    public EditGroupCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get the group
        Usergroup group
                = getSafeForWriting(
                actingAccount.getAllGroups().get(input.group.id));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check permissions
        if (!group.checkPermissionOfMember(actingAccount,
                Permission.EDIT_GROUP))
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITGROUP);

        // edit name
        group.setName(input.group.name);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final UsergroupEditDescription group;

        public Input(@JsonProperty("group") UsergroupEditDescription group,
                     @JsonProperty("token") String token) {
            super(token);
            this.group = group;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }
}
