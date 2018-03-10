package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteMembership;
import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class GroupMembershipChangeUpdate extends Update {

    private final CompleteMembership membershipDescription;

    public GroupMembershipChangeUpdate(@JsonProperty("time-created")
                                               Date timeCreated,
                                       @JsonProperty("creator") int creator,
                                       @JsonProperty("membership")
                                               CompleteMembership
                                               membershipDescription) {
        super(timeCreated, creator);
        this.membershipDescription = membershipDescription;

    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        GroupMembership membership = new GroupMembership
                (membershipDescription.accountID, membershipDescription
                        .groupID);
        //TODO
//        membership.getSharingUntil()
//        membership.setSharing(membershipDescription.isSharing);
        repositorySet.userGroupRepository.updateMembership(membership);
    }
}
