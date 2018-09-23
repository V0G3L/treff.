package org.pispeb.treffpunkt.server.commands.descriptions;

import org.pispeb.treffpunkt.server.service.domain.Usergroup;

/**
 * Immutable complete description of a user group as specified in the
 * treffpunkt protocol document, lacking the member, event and poll arrays.
 * <p>
 * User group edit create commands should require an instance of this class as
 * input.
 */
public class UsergroupEditDescription {

    public final int id;
    public final String name;

    public UsergroupEditDescription(Usergroup usergroup) {
        this.id = usergroup.getId();
        this.name = usergroup.getName();
    }
}
