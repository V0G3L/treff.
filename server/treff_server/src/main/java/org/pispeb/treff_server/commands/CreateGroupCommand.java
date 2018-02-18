package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.descriptions.UsergroupCreateDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * a command to create a user group
 */
public class CreateGroupCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "create-group",
                CreateGroupCommand.class);
    }

    public CreateGroupCommand(AccountManager accountManager,
                              ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        int actingAccountId = input.getActingAccount().getID();

        // collect all specified IDs and actionAccount's ID in set,
        // removing duplicates
        // ensure ascending order for correct locking
        SortedSet<Integer> sortedMemberIDs = new TreeSet<>();
        for (int id : input.group.memberIDs)
            sortedMemberIDs.add(id);
        sortedMemberIDs.add(actingAccountId);


        // lock the accounts in the correct order and add them all to a set
        Set<Account> members = new HashSet<>();
        for (int memberId : sortedMemberIDs) {
            // lock the account with smallest id and add it to the set,
            Account currentAccount = getSafeForReading(this.accountManager
                    .getAccount(memberId));
            // if account has been deleted, return an 'id invalid'-error.
            // If it was the acting account, reject the token instead.
            if (currentAccount == null) {
                if (memberId == actingAccountId)
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
                else return new ErrorOutput(ErrorCode.USERIDINVALID);
            }
            members.add(currentAccount);
        }

        // TODO: check if all other members are in contacts

        // create the group
        Usergroup usergroup = input.getActingAccount()
                .createGroup(input.group.name, members);

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccountId,
                        usergroup);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    new HashSet<>(usergroup.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
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
    }

    public static class Output extends CommandOutput {

        @JsonProperty("id")
        final int groupId;

        Output(int groupId) {
            this.groupId = groupId;
        }
    }
}
