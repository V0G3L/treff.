package org.pispeb.treffpunkt.server.commands.updates;

import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescription;

import java.util.Date;

public class GroupMembershipChangeUpdate extends UpdateToSerialize {
    public final MembershipDescription membershipDescription;

    public GroupMembershipChangeUpdate(Date date, int creator,
                                       MembershipDescription
                                               membershipDescription) {
        super(UpdateType.GROUP_MEMBERSHIP_CHANGE.toString(),
                date, creator);
        this.membershipDescription = membershipDescription;

    }
}
