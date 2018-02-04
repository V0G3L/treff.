package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

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

        // lock the accounts in the correct order and add them all to a set
        input.memberIds.add(actingAccountId);
        TreeSet<Account> memberAccounts = new TreeSet<>();
        for (int memberId : input.memberIds) {
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
                .createGroup(input.name, memberArray);

        // respond
        return new Output(usergroup.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final String name;
        final TreeSet<Integer> memberIds;

        public Input(@JsonProperty("name") String name,
                     @JsonProperty("members") int[] memberIds,
                     @JsonProperty("token") String token) {
            super(token);
            this.name = name;
            this.memberIds = new TreeSet<>();
            for (int memberId : memberIds) {
                this.memberIds.add(memberId);
            }
        }
    }

    public static class Output extends CommandOutput {

        final int groupId;

        Output(int groupId) {
            this.groupId = groupId;
        }
    }
}
