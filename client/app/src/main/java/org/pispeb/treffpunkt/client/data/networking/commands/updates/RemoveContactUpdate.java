package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class RemoveContactUpdate extends Update {

    public RemoveContactUpdate(@JsonProperty("time-created") Date timeCreated,
                               @JsonProperty("creator") int creator) {
        super(timeCreated, creator);
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == user that removed our current user from his contacts
        repositorySet.userRepository.setIsFriend(creator, false);
    }
}
