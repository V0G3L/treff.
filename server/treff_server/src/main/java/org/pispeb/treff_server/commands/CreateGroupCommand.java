package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.descriptions.UsergroupCreateDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

// TODO needs to be tested

/**
 * a command to create a Usergroup
 */
public class CreateGroupCommand extends AbstractCommand {

    public CreateGroupCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        int actingAccountId = input.getActingAccount().getID();

        // collect all specified IDs and actionAccount's ID in set,
        // removing duplicates
        Set<Integer> members = new HashSet<>();
        for (int id : input.group.memberIDs)
            members.add(id);
        members.add(actingAccountId);

        // lock the accounts in the correct order and add them all to a set
        TreeSet<Account> memberAccounts = new TreeSet<>();
        for (int memberId : members) {
            // lock the account with smallest id to the set,
            // check if it still exists and add it to the set
            Account currentAccount = getSafeForReading(this.accountManager
                    .getAccount(memberId));
            if (currentAccount == null) {
                if (memberId == actingAccountId)
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
                else return new ErrorOutput(ErrorCode.USERIDINVALID);
            }
            memberAccounts.add(currentAccount);
        }

        // remove the actingAccount from the set and turn the set to an array
        memberAccounts.remove(input.getActingAccount());
        Account[] memberArray = memberAccounts.toArray(new Account[0]);

        // create the group
        Usergroup usergroup = input.getActingAccount()
                .createGroup(input.group.name, memberArray);

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
