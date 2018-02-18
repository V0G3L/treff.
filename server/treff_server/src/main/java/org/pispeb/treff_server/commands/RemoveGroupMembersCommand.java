package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * a command to remove Accounts from a Usergroup
 */
public class RemoveGroupMembersCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "remove-group-members",
                RemoveGroupMembersCommand.class);
    }

    public RemoveGroupMembersCommand(AccountManager accountManager,
                                     ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        Account actingAccount = input.getActingAccount();

        // lock the accounts in the correct order and add them all to a set
        input.memberIds.add(actingAccount.getID());
        TreeSet<Account> newMemberAccounts = new TreeSet<>();
        for (int memberId : input.memberIds) {
            // lock the account with smallest id to the set,
            // check if it still exists and add it to the set
            Account currentAccount = getSafeForReading(this.accountManager
                    .getAccount(memberId));
            if (currentAccount == null) {
                if (memberId == actingAccount.getID())
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
                else return new ErrorOutput(ErrorCode.USERIDINVALID);
            }
            newMemberAccounts.add(currentAccount);
        }

        // remove the actingAccount from the set
        newMemberAccounts.remove(actingAccount);


        // get group
        Usergroup usergroup
                = getSafeForWriting(actingAccount.getAllGroups().get(input.id));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.MANAGE_MEMBERS)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONMANAGEMEMBERS);
        }

        // check if a new member is not part of the group
        for (int memberID : usergroup.getAllMembers().keySet()) {
            if (!input.memberIds.contains(memberID))
                return new ErrorOutput(ErrorCode.USERNOTINGROUP);
        }

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);
        for (Account a: usergroup.getAllMembers().values())
            getSafeForWriting(a);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    new HashSet<>(usergroup.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        // remove members from the group
        for (Account member : newMemberAccounts) {
            usergroup.removeMember(member);
        }

        //respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;
        final TreeSet<Integer> memberIds;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("members") int[] memberIds,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
            this.memberIds = new TreeSet<>();
            for (int memberId : memberIds) {
                this.memberIds.add(memberId);
            }
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
