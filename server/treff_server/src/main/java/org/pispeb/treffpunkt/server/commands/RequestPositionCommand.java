package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PositionRequestUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to request the position of an account
 */
public class RequestPositionCommand extends
        GroupCommand<RequestPositionCommand.Input, RequestPositionCommand.Output> {


    public RequestPositionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        // check time
        if (checkTime(input.time) < 0) {
            throw ErrorCode.TIMEENDINPAST.toWebException();
        }

        PositionRequestUpdate update =
                new PositionRequestUpdate(new Date(),
                        actingAccount.getID(), input.time);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        final Date time;

        public Input(int groupId, long time, String token) {
            super(token, groupId);
            this.time = new Date(time);
        }
    }

    public static class Output extends CommandOutput { }
}
