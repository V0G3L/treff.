package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.commands.descriptions.UsergroupComplete;
import org.pispeb.treff_server.commands.deserializers
        .UsergroupWithoutIDDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

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


        Set<Integer> members = input.group.getAllMembers().keySet();
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
                .createGroup(input.group.getName(), memberArray);

        // respond
        return new Output(usergroup.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final UsergroupComplete group;

        public Input(@JsonDeserialize(using
                = UsergroupWithoutIDDeserializer.class)
                     @JsonProperty("group") UsergroupComplete group,
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
