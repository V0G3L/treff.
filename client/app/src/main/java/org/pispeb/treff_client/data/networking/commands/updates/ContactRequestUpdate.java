package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.User;
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

        // check if user already in repository
        User user = repositorySet.userRepository.getUser(creator);
        if (user != null)
            repositorySet.userRepository.setIsRequesting(creator, true);
        else {
            User.getPlaceholderAndScheduleQuery(creator,
                    repositorySet.userRepository, u -> u.setRequesting(true));
        }
    }
}
