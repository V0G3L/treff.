package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.descriptions.UsergroupCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * a command to create a user group
 */
public class CreateGroupCommand extends AbstractCommand {


    public CreateGroupCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        Account actingAccount = input.getActingAccount();

        // check that all IDs are valid and remove duplicates
        Set<Account> members = new HashSet<>();
        for (int memberId : input.group.memberIDs) {
            Account currentAccount = accountManager.getAccount(memberId);
            if (currentAccount == null) {
                return new ErrorOutput(ErrorCode.USERIDINVALID);
            }
            members.add(currentAccount);
        }

        // TODO: check if all other members are in contacts

        // create the group
        Usergroup usergroup = actingAccount.createGroup(input.group.name, members, session);

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);
        try {
            HashSet<Account> affected =
                    new HashSet<>(usergroup.getAllMembers().values());
            affected.remove(input.getActingAccount());
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affected);
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }

        // respond
        return new Output(usergroup.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final UsergroupCreateDescription group;

        public Input(@JsonProperty("group") UsergroupCreateDescription group,
                     @JsonProperty("token") String token) {
            super(token);
            this.group = group;
        }

        @Override
        public boolean syntaxCheck() {
            return validateGroupName(group.name);
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("id")
        final int groupId;

        Output(int groupId) {
            this.groupId = groupId;
        }
    }
}
