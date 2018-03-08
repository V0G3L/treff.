package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.updates.ChatUpdate;
import org.pispeb.treff_server.interfaces.AccountManager;

import java.util.Date;

/**
 * a command to send a chat message to a user group
 */
public class SendChatMessageCommand extends GroupCommand {


    public SendChatMessageCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.READ_LOCK,
                null, null); // chatting requires no permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        ChatUpdate update =
                new ChatUpdate(new Date(), actingAccount.getID(),
                        usergroup.getID(), input.message);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupInput {

        final String message;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("message") String message,
                     @JsonProperty("token") String token) {
            super(token, groupId, new int[0]);
            this.message = message;
        }
    }

    public static class Output extends CommandOutput {
    }
}
