package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.serializers.UsergroupCompleteSerializer;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of a user group
 */
public class GetGroupDetailsCommand extends AbstractCommand {


    public GetGroupDetailsCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get group
        Usergroup group
                = getSafeForReading(actingAccount.getAllGroups()
                .get(input.groupId));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        return new Output(group);
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = UsergroupCompleteSerializer.class)
        @JsonProperty("group")
        final Usergroup usergroup;

        Output(Usergroup usergroup) {
            this.usergroup = usergroup;
        }
    }

}
