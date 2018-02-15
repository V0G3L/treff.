package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to update the Position of an Account in the database
 */
public class UpdatePositionCommand extends AbstractCommand {

    public UpdatePositionCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
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

        if (checkTime(input.timeMeasured) > 0) {
            return new ErrorOutput(ErrorCode.TIMEMEASUREDFUTURE);
        }

        // update position and respond
        actingAccount.updatePosition(input.position, input.timeMeasured);
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
