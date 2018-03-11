package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class CancelContactRequestUpdate extends Update {

    public CancelContactRequestUpdate(@JsonProperty("time-created") Date timeCreated,
                                      @JsonProperty("creator") int creator) {
        super(timeCreated, creator);
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == user that cancelled the contact request
        repositorySet.userRepository.setIsRequesting(creator, false);
    }
}
