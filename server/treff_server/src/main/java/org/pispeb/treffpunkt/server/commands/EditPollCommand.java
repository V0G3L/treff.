package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit a poll of a user group
 */
public class EditPollCommand extends PollCommand<EditPollCommand.Input, EditPollCommand.Output> {

    public EditPollCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPoll(Input input) {

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.EDIT_ANY_POLL) &&
                !(poll.getCreator().getID() == actingAccount.getID())) {
            throw ErrorCode.NOPERMISSIONEDITANYPOLL.toWebException();
        }

        // edit poll
        poll.setQuestion(input.poll.question);
        poll.setMultiChoice(input.poll.isMultiChoice);
        poll.setTimeVoteClose(input.poll.timeVoteClose);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends PollCommand.PollInput {

        final PollEditDescription poll;

        public Input(int groupId,
                             PollEditDescription poll, String token) {
            super(token, groupId, poll.id);
            this.poll = poll;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePollQuestion(poll.question)
                    && validateDate(poll.timeVoteClose);
        }
    }

    public static class Output extends CommandOutput { }
}
