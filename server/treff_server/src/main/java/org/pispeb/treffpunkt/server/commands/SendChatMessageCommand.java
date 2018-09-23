package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.ChatUpdate;

import java.util.Date;

/**
 * a command to send a chat message to a user group
 */
public class SendChatMessageCommand extends
        GroupCommand<SendChatMessageCommand.Input, SendChatMessageCommand.Output> {


    public SendChatMessageCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        ChatUpdate update =
                new ChatUpdate(new Date(), actingAccount.getID(),
                        usergroup.getID(), input.message);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        final String message;

        public Input(int groupId, String message, String token) {
            super(token, groupId);
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
