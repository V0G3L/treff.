package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.GroupMembershipChangeUpdate;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to share the position of the executing account with a specific
 * group during a specific time
 */
public class PublishPositionCommand extends GroupCommand {

    public PublishPositionCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.READ_LOCK,
                null, null); // position publishing requires no permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // check time
        if (checkTime(input.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // publish position
        usergroup.setLocationSharingTimeEndOfMember(
                actingAccount, input.timeEnd);

        // create update
        MembershipDescription mB
                = new MembershipDescription(usergroup.getID(),
                actingAccount.getID(), input.timeEnd.getTime(),
                usergroup.getPermissionsOfMember(actingAccount)
        );
        GroupMembershipChangeUpdate update =
                new GroupMembershipChangeUpdate(new Date(),
                        actingAccount.getID(),
                        mB);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupInput {

        final Date timeEnd;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("time-end") long timeEnd,
                     @JsonProperty("token") String token) {
            super(token, groupId, new int[0]);
            this.timeEnd = new Date(timeEnd);
        }
    }

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
