package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.descriptions.MembershipDescription;

import java.util.Date;

public class GroupMembershipChangeUpdate extends UpdateToSerialize {
    @JsonProperty("membership")
    public final MembershipDescription membershipDescription;

    public GroupMembershipChangeUpdate(Date date, int creator,
                                       MembershipDescription
                                               membershipDescription) {
        super(UpdateType.GROUP_MEMBERSHIP_CHANGE.toString(),
                date, creator);
        this.membershipDescription = membershipDescription;

    }
}
