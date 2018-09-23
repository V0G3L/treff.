package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollOptionEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit an option of a poll
 */
public class EditPollOptionCommand
        extends PollOptionCommand<EditPollOptionCommand.Input, EditPollOptionCommand.Output> {

    public EditPollOptionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPollOption(Input input) {

        // check times
        if (input.inputOption.timeEnd
                .before(input.inputOption.timeStart)) {
            throw ErrorCode.TIMEENDSTARTCONFLICT.toWebException();
        }

        if (checkTime(input.inputOption.timeEnd) < 0) {
            throw ErrorCode.TIMEENDINPAST.toWebException();
        }

        // check permission
        if (poll.getCreator().getID() != actingAccount.getID() &&
                !usergroup.checkPermissionOfMember(actingAccount,
                        Permission.EDIT_ANY_POLL)) {
            throw ErrorCode.NOPERMISSIONEDITANYPOLL.toWebException();
        }

        // edit poll option
        pollOption.setPosition(input.inputOption.position);
        pollOption.setTimeStart(input.inputOption.timeStart);
        pollOption.setTimeEnd(input.inputOption.timeEnd);

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends PollOptionCommand.PollOptionInput {

        final PollOptionEditDescription inputOption;

        public Input(int groupId, int pollId,
                             PollOptionEditDescription inputOption, String token) {
            super(token, groupId, pollId, inputOption.id);
            this.inputOption = inputOption;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePosition(inputOption.position)
                    && validateDate(inputOption.timeStart)
                    && validateDate(inputOption.timeEnd);
        }
    }

    public static class Output extends CommandOutput {
    }

}
