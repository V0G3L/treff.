package org.pispeb.treffpunkt.server.commands.descriptions;

import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import java.util.List;

/**
 * Immutable complete description of a user group as specified in the
 * treffpunkt protocol document, lacking the id property and the event and poll
 * arrays.
 * <p>
 * User group option create commands should require an instance of this class as
 * input.
 */
public class UsergroupCreateDescription {

    public final String name;
    public final List<Integer> memberIDs;

    public UsergroupCreateDescription(Usergroup usergroup) {
        this.name = usergroup.getName();
        this.memberIDs = usergroup.getMemberIDs();
    }
}
