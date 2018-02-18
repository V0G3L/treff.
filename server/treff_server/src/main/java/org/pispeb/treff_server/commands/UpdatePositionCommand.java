package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to update the position of the executing account in the database
 */
public class UpdatePositionCommand extends AbstractCommand {


    public UpdatePositionCommand(AccountManager accountManager,
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

        if (checkTime(input.timeMeasured) > 0) {
            return new ErrorOutput(ErrorCode.TIMEMEASUREDFUTURE);
        }

        // update position and respond
        actingAccount.updatePosition(input.position, input.timeMeasured);

        // create update
        Set<Account> affected = new HashSet<Account>();
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            getSafeForReading(g);
            if (new Date().before(
                    g.getLocationSharingTimeEndOfMember(actingAccount))) {
                affected.addAll(g.getAllMembers().values());
            }
        }
        for (Account a:affected)
            getSafeForWriting(a);
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.POSITION);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    affected);
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final Position position;
        final Date timeMeasured;

        public Input(@JsonProperty("latitude") double latitude,
                     @JsonProperty("longitude") double longitude,
                     @JsonProperty("time-measured") long timeMeasured,
                     @JsonProperty("token") String token) {
            super(token);
            this.position = new Position(latitude, longitude);
            this.timeMeasured = new Date(timeMeasured);
        }
    }

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
