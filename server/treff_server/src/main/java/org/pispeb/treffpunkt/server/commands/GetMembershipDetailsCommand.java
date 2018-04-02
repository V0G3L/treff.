package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to get the group membership of an account to a specific user group
 */
public class GetMembershipDetailsCommand extends GroupCommand {


    public GetMembershipDetailsCommand(SessionFactory sessionFactory,
                                       ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper,
                null, null);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        Input input = (Input) commandInput;

        Account account = accountManager.getAccount(input.id);
        if (account == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        if (!usergroup.getAllMembers().containsKey(account.getID()))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

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

    public static class Input extends GroupInput {

        final int id;
        final int groupId;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId);
            this.id = id;
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("membership")
        public final MembershipDescription membershipDescription;

        Output(MembershipDescription membershipDescription) {
            this.membershipDescription = membershipDescription;
        }
    }

}
