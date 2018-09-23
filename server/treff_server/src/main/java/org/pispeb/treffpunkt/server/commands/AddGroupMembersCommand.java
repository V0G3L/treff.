package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

/**
 * a command to add accounts to a user group
 */
public class AddGroupMembersCommand extends ChangeGroupMembersCommand {

    public AddGroupMembersCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input groupInput) {
        return changeGroupMembers(groupInput, true);
    }
}
