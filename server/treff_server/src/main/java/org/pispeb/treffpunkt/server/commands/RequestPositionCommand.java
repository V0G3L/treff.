package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PositionRequestUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to request the position of an account
 */
public class RequestPositionCommand extends GroupCommand {


    public RequestPositionCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
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
            super(token, groupId);
            this.time = new Date(time);
        }
    }

    public static class Output extends CommandOutput { }
}
