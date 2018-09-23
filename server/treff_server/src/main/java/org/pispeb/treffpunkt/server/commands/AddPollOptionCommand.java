package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollOptionCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to add an poll option to a poll
 */
public class AddPollOptionCommand
        extends PollCommand<AddPollOptionCommand.Input, AddPollOptionCommand.Output> {


    public AddPollOptionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPoll(Input input) {

        // check times
        if (input.pollOption.timeEnd
                .before(input.pollOption.timeStart)) {
            throw ErrorCode.TIMEENDSTARTCONFLICT.toWebException();
        }

        if (checkTime(input.pollOption.timeEnd) < 0) {
            throw ErrorCode.TIMEENDINPAST.toWebException();
        }

        // check permission
        if (poll.getCreator().getID() != actingAccount.getID() &&
                !usergroup.checkPermissionOfMember(actingAccount,
                        Permission.EDIT_ANY_POLL)) {
            throw ErrorCode.NOPERMISSIONEDITANYPOLL.toWebException();
        }

        // add poll option
        PollOption pO = poll.addPollOption(input.pollOption.position,
                input.pollOption.timeStart, input.pollOption.timeEnd, session);

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pO);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output(pO.getID());
    }

    public static class Input extends PollCommand.PollInput{

        final PollOptionCreateDescription pollOption;

        public Input(int groupId, int pollId,
                             PollOptionCreateDescription pollOption, String token) {
            super(token, groupId, pollId);
            this.pollOption = pollOption;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePosition(pollOption.position)
                    && validateDate(pollOption.timeStart)
                    && validateDate(pollOption.timeEnd);
        }
    }

    public static class Output extends CommandOutput {
        final int pollId;

        Output(int pollId) {
            this.pollId = pollId;
        }
    }

}
