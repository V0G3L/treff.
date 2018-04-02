package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;

/**
 * a command to add accounts to a user group
 */
public class AddGroupMembersCommand extends ChangeGroupMembersCommand {

    public AddGroupMembersCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory, mapper);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        return changeGroupMembers(groupInput, true);
    }
}
