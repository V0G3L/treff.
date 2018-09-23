package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PositionChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;
import org.pispeb.treffpunkt.server.service.domain.Position;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to update the position of the executing account in the database
 */
public class UpdatePositionCommand extends AbstractCommand
        <UpdatePositionCommand.Input,UpdatePositionCommand.Output> {


    public UpdatePositionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();
        Position position = input.position;

        if (checkTime(position.getTimeMeasured()) > 0) {
            throw ErrorCode.TIMEMEASUREDFUTURE.toWebException();
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
        actingAccount.updatePosition(input.position);

        // create update
        PositionChangeUpdate update =
                new PositionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        position);
        accountManager.createUpdate(update, affectedAccounts);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final Position position;

        public Input(Position position, String token) {
            super(token);
            this.position = position;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePosition(position);
        }
    }

    public static class Output extends CommandOutput { }
}
