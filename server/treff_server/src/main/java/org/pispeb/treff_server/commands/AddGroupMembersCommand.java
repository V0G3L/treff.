package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("id", 0)
                        .add("members", Json.createArrayBuilder()
                                .add(0)
                                .build())
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              int actingAccountID) {
        int id = input.getInt("id");

        // get the IDs of all new members
        JsonArray newMembersArray = input.getJsonArray("members");
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
                    return new CommandResponse(StatusCode.USERIDINVALID);
            } else {
                // check if the acting account still exists
                actingAccount = getSafeForReading(this.accountManager
                        .getAccount(actingAccountID));
                if (actingAccount == null)
                    return new CommandResponse(StatusCode.TOKENINVALID);
                actingAccountLocked = true;
            }
        }

        // get group
        Usergroup usergroup
                = getSafeForWriting(actingAccount.getAllGroups().get(id));
        if (usergroup == null)
            return new CommandResponse(StatusCode.GROUPIDINVALID);

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.MANAGE_MEMBERS)) {
            return new CommandResponse(StatusCode.NOPERMISSIONMANAGEMEMBERS);
        }

        // check if a new member is already part of the group
        for (int memberID : usergroup.getAllMembers().keySet()) {
            if (sortedNewMemberIDs.contains(memberID))
                return new CommandResponse(StatusCode.USERALREADYINGROUP);
        }

        // add all new members to the group
        for (Account newMember : newMembersMap.values()) {
            usergroup.addMember(newMember);
        }

        //respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
