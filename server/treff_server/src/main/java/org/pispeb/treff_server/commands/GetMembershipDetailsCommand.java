package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to get the group membership of an account to a specific user group
 */
public class GetMembershipDetailsCommand extends GroupCommand {


    public GetMembershipDetailsCommand(AccountManager accountManager,
                                       ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.READ_LOCK,
                null,null);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        Input input = (Input) commandInput;

        Account account = accountManager.getAccount(input.id);
        if (null == account
                .getAllGroups().get(input.groupId))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

        Date d = usergroup.getLocationSharingTimeEndOfMember(account);
        long locationSharingTime = (d == null)?0:d.getTime();
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
            super(token, groupId, new int[]{id});
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
