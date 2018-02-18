package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to get the group membership of an account to a specific user group
 */
public class GetMembershipDetailsCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "get-membership-details",
                GetMembershipDetailsCommand.class);
    }

    public GetMembershipDetailsCommand(AccountManager accountManager,
                                       ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {

        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get account to check
        Account account =
                getSafeForReading(accountManager.getAccount(input.id));
        if (account == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);

        // get group
        Usergroup group
                = getSafeForReading(actingAccount.getAllGroups()
                .get(input.groupId));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        if (null == account.getAllGroups().get(input.groupId))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

        MembershipDescription mB =
                new MembershipDescription(actingAccount.getID(),
                        group.getPermissionsOfMember(actingAccount),
                        group.getLocationSharingTimeEndOfMember(actingAccount)
                                .getTime());
        return new Output(mB);
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;
        final int groupId;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
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
