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
 * a command to add accounts to a user group
 */
public class AddGroupMembersCommand extends AbstractCommand {


    public AddGroupMembersCommand(AccountManager accountManager,
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
                return new ErrorOutput(ErrorCode.USERIDINVALID);
            }
            newMemberAccounts.add(currentAccount);
        }

        // remove the actingAccount from the set
        newMemberAccounts.remove(actingAccount);

        // get group
        Usergroup usergroup
                = getSafeForWriting(actingAccount
                .getAllGroups().get(input.groupId));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.MANAGE_MEMBERS)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONMANAGEMEMBERS);
        }

        // check if a new member is already part of the group
        for (int memberID : usergroup.getAllMembers().keySet()) {
            if (input.memberIds.contains(memberID))
                return new ErrorOutput(ErrorCode.USERALREADYINGROUP);
        }

        // add all new members to the group
        for (Account newMember : newMemberAccounts) {
            usergroup.addMember(newMember);
        }

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);
        for (Account a : usergroup.getAllMembers().values()) {
            if (a.getID() == actingAccount.getID())
                continue;
            getSafeForReading(a);
        }
        try {
            HashSet<Account> affected =
                    new HashSet<>(usergroup.getAllMembers().values());
            affected.remove(actingAccount);
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    affected);
        } catch (JsonProcessingException e) {
            // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        //respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final TreeSet<Integer> memberIds;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("members") int[] memberIds,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
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
