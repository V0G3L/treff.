package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteMembership;

import java.util.Date;

public class GroupMembershipChangeUpdate extends UpdateToSerialize {
    @JsonProperty("membership")
    public final CompleteMembership membershipDescription;

    public GroupMembershipChangeUpdate(@JsonProperty("type") String type,
                                       @JsonProperty("time-created") Date date,
                                       @JsonProperty("creator") int creator,
                                       @JsonProperty("membership") CompleteMembership membershipDescription) {
        super(UpdateType.GROUP_MEMBERSHIP_CHANGE.toString(),
                date, creator);
        this.membershipDescription = membershipDescription;

    }
}
