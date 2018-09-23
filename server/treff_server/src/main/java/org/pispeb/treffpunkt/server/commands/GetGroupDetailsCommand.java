package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of a user group
 */
public class GetGroupDetailsCommand extends
        GroupCommand<GetGroupDetailsCommand.Input, GetGroupDetailsCommand.Output> {


    public GetGroupDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        Account actingAccount = input.getActingAccount();

        // get group
        Usergroup group = actingAccount.getAllGroups().get(input.groupID);
        if (group == null)
            throw ErrorCode.GROUPIDINVALID.toWebException();

        return new Output(group);
    }

    public static class Input extends GroupCommand.GroupInput {

        public Input(int groupId, String token) {
            super(token, groupId);
        }
    }

    public static class Output extends CommandOutput {

        public final org.pispeb.treffpunkt.server.service.domain.Usergroup usergroup;

        Output(Usergroup usergroup) {
            this.usergroup = new org.pispeb.treffpunkt.server.service.domain.Usergroup(usergroup);
        }
    }

}
