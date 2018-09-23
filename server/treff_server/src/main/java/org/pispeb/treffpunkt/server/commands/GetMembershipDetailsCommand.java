package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to get the group membership of an account to a specific user group
 */
public class GetMembershipDetailsCommand extends
        GroupCommand<GetMembershipDetailsCommand.Input, GetMembershipDetailsCommand.Output> {


    public GetMembershipDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        Account account = accountManager.getAccount(input.id);
        if (account == null)
            throw ErrorCode.USERIDINVALID.toWebException();
        if (!usergroup.getAllMembers().containsKey(account.getID()))
            throw ErrorCode.USERNOTINGROUP.toWebException();

        // assemble output
        Date d = usergroup.getLocationSharingTimeEndOfMember(account);
        long locationSharingTime = (d == null) ? 0 : d.getTime();
        MembershipDescription mB =
                new MembershipDescription(usergroup.getID(), account.getID(),
                        locationSharingTime,
                        usergroup.getPermissionsOfMember(account)
                );
        return new Output(mB);
    }

    public static class Input extends GroupCommand.GroupInput {

        final int id;
        final int groupId;

        public Input(int id, int groupId, String token) {
            super(token, groupId);
            this.id = id;
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {

        public final MembershipDescription membershipDescription;

        Output(MembershipDescription membershipDescription) {
            this.membershipDescription = membershipDescription;
        }
    }

}
