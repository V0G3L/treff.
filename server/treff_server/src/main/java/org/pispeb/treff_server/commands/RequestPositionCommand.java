package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PositionRequestUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to request the position of an account
 */
public class RequestPositionCommand extends GroupCommand {


    public RequestPositionCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.READ_LOCK,
                null, null); // requesting position requires no permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // check time
        if (checkTime(input.time) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        PositionRequestUpdate update =
                new PositionRequestUpdate(new Date(),
                        actingAccount.getID(), input.time);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupInput {

        final Date time;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("time") long time,
                     @JsonProperty("token") String token) {
            super(token, groupId, new int[0]);
            this.time = new Date(time);
        }
    }

    public static class Output extends CommandOutput {
    }
}
