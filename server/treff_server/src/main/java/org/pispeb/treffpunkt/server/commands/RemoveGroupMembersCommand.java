package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

/**
 * a command to remove accounts from a user group
 */
public class RemoveGroupMembersCommand extends ChangeGroupMembersCommand {

    public RemoveGroupMembersCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {
        return changeGroupMembers(input, false);
    }

}
