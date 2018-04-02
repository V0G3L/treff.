package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.ChatUpdate;

import java.util.Date;

/**
 * a command to send a chat message to a user group
 */
public class SendChatMessageCommand extends GroupCommand {


    public SendChatMessageCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
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

        @Override
        public boolean syntaxCheck() {
            // TODO: is this unicode symbols or byte length?
            return validateChatMessage(message);
        }
    }

    public static class Output extends CommandOutput {
    }
}
