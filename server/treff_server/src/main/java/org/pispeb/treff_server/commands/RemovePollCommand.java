package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollChangeUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to delete a poll of a user group
 */
public class RemovePollCommand extends AbstractCommand {


    public RemovePollCommand(AccountManager accountManager,
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
        Usergroup group =
                getSafeForWriting(actingAccount.getAllGroups()
                        .get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls().get(input.pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        poll);
        for (Account a: group.getAllMembers().values())
            getSafeForWriting(a);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
        }

        // remove the poll
        poll.delete();

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int pollId;
        final int groupId;

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
            this.pollId = pollId;
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
