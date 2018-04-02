package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.*;

/**
 * a command to remove accounts from a user group
 */
public class RemoveGroupMembersCommand extends ChangeGroupMembersCommand {


    public RemoveGroupMembersCommand(SessionFactory sessionFactory,
                                     ObjectMapper mapper) {
        super(sessionFactory, mapper);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        return changeGroupMembers(groupInput, false);
    }

}
