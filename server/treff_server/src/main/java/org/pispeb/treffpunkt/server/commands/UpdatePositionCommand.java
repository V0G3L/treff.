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

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        if (checkTime(input.timeMeasured) > 0) {
            return new ErrorOutput(ErrorCode.TIMEMEASUREDFUTURE);
        }

        // collect accounts that need to be locked (actingAccount and all
        // members of groups the actingAccount is a member of and is sharing
        // its position with)
        // have to re-check existence after lock (re-)acquisition
        SortedSet<Account> accountsToLock = new TreeSet<>();
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            getSafeForReading(g);
            Date sharingUntil
                    = g.getLocationSharingTimeEndOfMember(actingAccount);
            if (sharingUntil != null && new Date().before(sharingUntil)) {
                accountsToLock.addAll(g.getAllMembers().values());
            }
            releaseReadLock(g);
        }

        // have to re-acquire all locks in correct locking order
        releaseReadLock(actingAccount);
        Set<Account> affected = new HashSet<>();
        for (Account account : accountsToLock) {
            // write lock for actingAccount since property has to be updated
            // all other accounts are read-locked for update addition
            if (account.getID() == actingAccount.getID()) {
                account = getSafeForWriting(account);
                if (account == null)
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
            } else {
                account = getSafeForReading(account);
                // add successfully locked other accounts to set of affected
                // accounts for update addition
                if (account != null)
                    affected.add(account);
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
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affected);
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

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
