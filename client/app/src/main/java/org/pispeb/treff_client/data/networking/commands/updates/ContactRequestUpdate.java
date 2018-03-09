package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class ContactRequestUpdate extends Update {

    public ContactRequestUpdate(@JsonProperty("time-created") Date timeCreated,
                                @JsonProperty("creator") int creator) {
        super(timeCreated, creator);
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == request sender
        repositorySet.userRepository.setIsRequesting(creator, true);
    }
}
