package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;
import org.pispeb.treff_server.commands.descriptions.MembershipEditDescription;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.GroupMembershipChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit the permissions of an account
 */
public class EditMembershipCommand extends GroupCommand {


    public EditMembershipCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.READ_LOCK,
                Permission.CHANGE_PERMISSIONS,
                ErrorCode.NOPERMISSIONEDITPERMISSION);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        Input input = (Input) commandInput;

        Account account = getSafeForReading(accountManager
                .getAccount(input.membershipEditDescription.accountID));
        if (null == account)
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        if(null == account.getAllGroups().get(usergroup.getID()))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

        // edit permissions
        for (Permission p
                : input.membershipEditDescription.permissionMap.keySet()) {
            usergroup.setPermissionOfMember(account, p,
                    input.membershipEditDescription.permissionMap.get(p));
        }

        Date d = usergroup.getLocationSharingTimeEndOfMember(account);
        long locationSharingTime = (d == null)?0:d.getTime();
        MembershipDescription mB =
                new MembershipDescription(usergroup.getID(), account.getID(),
                        locationSharingTime,
                        usergroup.getPermissionsOfMember(account)
                );
        GroupMembershipChangeUpdate update =
                new GroupMembershipChangeUpdate(new Date(),
                        actingAccount.getID(),
                        mB);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupInput {

        final MembershipEditDescription membershipEditDescription;

        public Input(@JsonProperty("membership")
                             MembershipEditDescription mB,
                     @JsonProperty("token") String token) {
            super(token, mB.groupID);
            this.membershipEditDescription = mB;
        }
    }

    public static class Output extends CommandOutput {
    }
}
