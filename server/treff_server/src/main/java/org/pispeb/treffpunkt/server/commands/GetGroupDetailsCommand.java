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
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of a user group
 */
public class GetGroupDetailsCommand extends GroupCommand {


    public GetGroupDetailsCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper,
                null, null); // getting details requires no permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        Account actingAccount = input.getActingAccount();

        // get group
        Usergroup group = actingAccount.getAllGroups().get(input.groupID);
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        return new Output(group);
    }

    public static class Input extends GroupInput {

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId);
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
