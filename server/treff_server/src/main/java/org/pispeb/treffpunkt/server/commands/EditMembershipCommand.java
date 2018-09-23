package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.GroupMembershipChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit the permissions of an account
 */
public class EditMembershipCommand
        extends GroupCommand<EditMembershipCommand.Input, EditMembershipCommand.Output> {


    public EditMembershipCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        Account otherAccount = accountManager.getAccount(input.membershipEditDescription.accountID);
        if (otherAccount == null)
            throw ErrorCode.USERIDINVALID.toWebException();
        if (!usergroup.getAllMembers().containsKey(otherAccount.getID()))
            throw ErrorCode.USERNOTINGROUP.toWebException();

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

    public static class Input extends GroupCommand.GroupInput {

        final MembershipEditDescription membershipEditDescription;

        public Input(MembershipEditDescription mB, String token) {
            super(token, mB.groupID);
            this.membershipEditDescription = mB;
        }
    }

    public static class Output extends CommandOutput { }
}
