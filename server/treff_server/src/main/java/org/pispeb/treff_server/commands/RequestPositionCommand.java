package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PositionRequestUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to request the Position of an Account
 */
public class RequestPositionCommand extends AbstractCommand {

    public RequestPositionCommand(AccountManager accountManager, ObjectMapper mapper) {
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

        //TODO

        // create update
        PositionRequestUpdate update =
                new PositionRequestUpdate(new Date(),
                        actingAccount.getID(), input.time);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    (Account[]) group.getAllMembers().values().toArray());
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        return new Output(); //TODO
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
