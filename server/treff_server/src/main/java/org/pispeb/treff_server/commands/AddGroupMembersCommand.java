package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import javax.json.JsonArray;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO needs to be tested

/**
 * a command to add Accounts to a Usergroup
 */
public class AddGroupMembersCommand extends AbstractCommand {

    public AddGroupMembersCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int id = 0; //input.getInt("id");
        int actingAccountID = 0; // TODO: migrate

        // get the IDs of all new members
        JsonArray newMembersArray = null; //input.getJsonArray("members");
        SortedSet<Integer> sortedNewMemberIDs = new TreeSet<>();
        for (int n = 0; n < newMembersArray.size(); n++) {
            sortedNewMemberIDs.add(newMembersArray.getJsonNumber(n).intValue());
        }

        // determine locking order for the Accounts
        boolean actingAccountLocked = false;
        Account actingAccount = null;
        Map<Integer, Account> newMembersMap = new HashMap<>();
        while (!sortedNewMemberIDs.isEmpty()) {
            int smallestNewMemberID = sortedNewMemberIDs.first();
            if (smallestNewMemberID < actingAccountID || actingAccountLocked) {
                // add account with smallest id to the set
                // and check if it still exists
                newMembersMap.put(smallestNewMemberID,
                        getSafeForReading(this.accountManager
                                .getAccount(smallestNewMemberID)));
                if (newMembersMap.get(smallestNewMemberID) == null)
                    return new ErrorOutput(ErrorCode.USERIDINVALID);
            } else {
                // check if the acting account still exists
                actingAccount = getSafeForReading(this.accountManager
                        .getAccount(actingAccountID));
                if (actingAccount == null)
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
                actingAccountLocked = true;
            }
        }

        // get group
        Usergroup usergroup
                = getSafeForWriting(actingAccount.getAllGroups().get(id));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.MANAGE_MEMBERS)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONMANAGEMEMBERS);
        }

        // check if a new member is already part of the group
        for (int memberID : usergroup.getAllMembers().keySet()) {
            if (sortedNewMemberIDs.contains(memberID))
                return new ErrorOutput(ErrorCode.USERALREADYINGROUP);
        }

        // add all new members to the group
        for (Account newMember : newMembersMap.values()) {
            usergroup.addMember(newMember);
        }

        //respond
        throw new UnsupportedOperationException();
    }

}
