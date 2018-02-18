package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.serializers.UsergroupShallowSerializer;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

/**
 * a command to get all Usergroups of an Account
 */
public class ListGroupsCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "list-groups",
                ListGroupsCommand.class);
    }

    public ListGroupsCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get groups
        Set<Usergroup> safeGroups = new HashSet<>();
        for (Usergroup group : actingAccount.getAllGroups().values()) {
            group = getSafeForReading(group);
            if (group == null) {
                return new ErrorOutput(ErrorCode.GROUPIDINVALID);
            }
            safeGroups.add(group);
        }

        //respond
        Usergroup[] outputArray = safeGroups.toArray(new Usergroup[0]);
        return new Output(outputArray);
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(@JsonProperty("token") String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(contentUsing = UsergroupShallowSerializer.class)
        @JsonProperty("groups")
        final Usergroup[] groups;

        Output(Usergroup[] groups) {
            this.groups = groups;
        }
    }
}
