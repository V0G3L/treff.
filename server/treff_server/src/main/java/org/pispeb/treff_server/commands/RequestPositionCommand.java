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
public class RequestPositionCommand extends AbstractCommand {


    public RequestPositionCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups()
                        .get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // check time
        if (checkTime(input.time) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // create update
        HashSet<? extends Account> affectedUsers
                = new HashSet<>(group.getAllMembers().values());
        affectedUsers.remove(actingAccount);

        PositionRequestUpdate update =
                new PositionRequestUpdate(new Date(),
                        actingAccount.getID(), input.time);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affectedUsers);
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final Date time;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("time") long time,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.time = new Date(time);
        }
    }

    public static class Output extends CommandOutput {
    }
}
