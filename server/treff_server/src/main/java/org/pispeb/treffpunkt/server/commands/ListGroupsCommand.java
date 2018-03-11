package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.serializers.UsergroupShallowSerializer;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

/**
 * a command to get all user groups of an account
 */
public class ListGroupsCommand extends AbstractCommand {


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
