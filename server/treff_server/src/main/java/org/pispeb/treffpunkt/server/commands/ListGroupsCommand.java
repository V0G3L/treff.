package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.serializers.UsergroupShallowSerializer;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

/**
 * a command to get all user groups of an account
 */
public class ListGroupsCommand extends AbstractCommand {

    public ListGroupsCommand(SessionFactory sessionFactory,
                             ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;

        Account actingAccount = input.getActingAccount();

        // get groups
        Usergroup[] outputArray = actingAccount.getAllGroups().values().toArray(new Usergroup[0]);
        return new Output(outputArray);
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(@JsonProperty("token") String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(contentUsing = UsergroupShallowSerializer.class)
        @JsonProperty("groups")
        final Usergroup[] groups;

        Output(Usergroup[] groups) {
            this.groups = groups;
        }
    }
}
