package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializer.UsergroupCompleteSerializer;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to get a detailed description of a Usergroup
 */
public class GetGroupDetailsCommand extends AbstractCommand {

    public GetGroupDetailsCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get group
        Usergroup group
                = getSafeForReading(actingAccount.getAllGroups()
                .get(input.groupId));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        return new Output(group);
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = UsergroupCompleteSerializer.class)
        final Usergroup usergroup;

        Output(Usergroup usergroup) {
            this.usergroup = usergroup;
        }
    }

}
