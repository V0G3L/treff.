package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to edit the name of a Usergroup
 */
public class EditGroupNameCommand extends AbstractCommand {

    public EditGroupNameCommand(AccountManager accountManager) {
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
                = getSafeForWriting(actingAccount.getAllGroups().get(input.id));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check permissions
        if (!group.checkPermissionOfMember(actingAccount,
                Permission.EDIT_GROUP))
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITGROUP);

        // edit name
        group.setName(input.name);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;
        final String name;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("name") String name,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
            this.name = name;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }
}
