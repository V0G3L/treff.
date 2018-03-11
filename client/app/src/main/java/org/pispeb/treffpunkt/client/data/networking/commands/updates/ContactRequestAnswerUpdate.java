package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class ContactRequestAnswerUpdate extends Update {

    private final Boolean answer;

    public ContactRequestAnswerUpdate(@JsonProperty("time-created") Date timeCreated,
                                      @JsonProperty("creator") int creator,
                                      @JsonProperty("answer") Boolean answer) {
        super(timeCreated, creator);
        this.answer = answer;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == user that answered the contact request
        repositorySet.userRepository.setIsPending(creator, false);
        repositorySet.userRepository.setIsFriend(creator, answer);
    }
}
