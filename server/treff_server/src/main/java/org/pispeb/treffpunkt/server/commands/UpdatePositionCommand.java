package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PositionChangeUpdate;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.*;

/**
 * a command to update the position of the executing account in the database
 */
public class UpdatePositionCommand extends AbstractCommand {


    public UpdatePositionCommand(SessionFactory sessionFactory,
                                 ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        Account actingAccount = input.getActingAccount();

        if (checkTime(input.timeMeasured) > 0) {
            return new ErrorOutput(ErrorCode.TIMEMEASUREDFUTURE);
        }

        Set<Account> affectedAccounts = new HashSet<>();
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            // only add members of groups in which location sharing is active
            Date sharingUntil = g.getLocationSharingTimeEndOfMember(actingAccount);
            if (sharingUntil != null && new Date().before(sharingUntil)) {
                affectedAccounts.addAll(g.getAllMembers().values());
            }
        }

        // update position and respond
        actingAccount.updatePosition(input.position, input.timeMeasured);

        // create update
        PositionChangeUpdate update =
                new PositionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        input.position,
                        input.timeMeasured);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update), affectedAccounts);
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
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

        @Override
        public boolean syntaxCheck() {
            return validatePosition(position)
                    && validateDate(timeMeasured);
        }
    }

    public static class Output extends CommandOutput { }
}
