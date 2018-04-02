package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.GroupMembershipChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit the permissions of an account
 */
public class EditMembershipCommand extends GroupCommand {


    public EditMembershipCommand(SessionFactory sessionFactory, ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper,
                Permission.CHANGE_PERMISSIONS,
                ErrorCode.NOPERMISSIONEDITPERMISSION);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        Input input = (Input) commandInput;

        Account otherAccount = accountManager.getAccount(input.membershipEditDescription.accountID);
        if (otherAccount == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        if (!usergroup.getAllMembers().containsKey(otherAccount.getID()))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

        // edit permissions
        for (Permission p
                : input.membershipEditDescription.permissionMap.keySet()) {
            usergroup.setPermissionOfMember(otherAccount, p,
                    input.membershipEditDescription.permissionMap.get(p));
        }

        // create Update
        Date d = usergroup.getLocationSharingTimeEndOfMember(otherAccount);
        long locationSharingTime = (d == null) ? 0 : d.getTime();
        MembershipDescription mB =
                new MembershipDescription(usergroup.getID(), otherAccount.getID(),
                        locationSharingTime,
                        usergroup.getPermissionsOfMember(otherAccount)
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

    public static class Output extends CommandOutput { }
}
