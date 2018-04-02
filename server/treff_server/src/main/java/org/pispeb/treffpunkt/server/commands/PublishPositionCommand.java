package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.GroupMembershipChangeUpdate;

import java.util.Date;

/**
 * a command to share the position of the executing account with a specific
 * group during a specific time
 */
public class PublishPositionCommand extends GroupCommand {

    public PublishPositionCommand(SessionFactory sessionFactory,
                                 ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                null, null); // position publishing requires no permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

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
            super(token, groupId);
            this.timeEnd = new Date(timeEnd);
        }

        @Override
        public boolean syntaxCheck() {
            return validateDate(timeEnd);
        }
    }

    public static class Output extends CommandOutput { }
}
