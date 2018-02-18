package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
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

/**
 * a command to edit the permissions of an Account
 */
public class EditMembershipCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "edit-membership",
                EditMembershipCommand.class);
    }

    public EditMembershipCommand(AccountManager accountManager, ObjectMapper mapper) {
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

        // get account to edit permissions
        Account account =
                getSafeForWriting(accountManager
                        .getAccount(input.membershipDescription.accountID));
        if (account == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);

        // get group
        Usergroup group
                = getSafeForWriting(actingAccount.getAllGroups()
                .get(input.groupId));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        if (null == account.getAllGroups().get(input.groupId))
            return new ErrorOutput(ErrorCode.USERNOTINGROUP);

        // check permission
        if (!group.checkPermissionOfMember(actingAccount,
                Permission.CHANGE_PERMISSIONS)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITPERMISSION);
        }

        // edit permissions
        for (Permission p
                : input.membershipDescription.permissionMap.keySet()) {
            group.setPermissionOfMember(account, p,
                    input.membershipDescription.permissionMap.get(p));
        }


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
        GroupMembershipChangeUpdate update =
                new GroupMembershipChangeUpdate(new Date(),
                        actingAccount.getID(),
                        input.membershipDescription);
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
        final MembershipDescription membershipDescription;

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("membership")
                             MembershipDescription mB,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.membershipDescription = mB;
        }
    }

    public static class Output extends CommandOutput {
    }
}
