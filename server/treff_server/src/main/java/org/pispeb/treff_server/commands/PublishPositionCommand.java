package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.GroupMembershipChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PublishPositionCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "publish-position",
                PublishPositionCommand.class);
    }

    public PublishPositionCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group = getSafeForWriting(
                actingAccount.getAllGroups().get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // publish position
        group.setLocationSharingTimeEndOfMember(actingAccount, input.timeEnd);

        // create update
        Set<Account> affected = new HashSet<Account>();
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            getSafeForReading(g);
            if (new Date().before(
                    g.getLocationSharingTimeEndOfMember(actingAccount))) {
                affected.addAll(g.getAllMembers().values());
            }
        }
        for (Account a : affected)
            getSafeForWriting(a);
        MembershipDescription mB
                = new MembershipDescription(actingAccount.getID(),
                    group.getPermissionsOfMember(actingAccount),
                input.timeEnd.getTime());
        GroupMembershipChangeUpdate update =
                new GroupMembershipChangeUpdate(new Date(),
                        actingAccount.getID(),
                        mB);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    affected);
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final Date timeEnd;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("time-end") long timeEnd,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.timeEnd = new Date(timeEnd);
        }
    }

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
