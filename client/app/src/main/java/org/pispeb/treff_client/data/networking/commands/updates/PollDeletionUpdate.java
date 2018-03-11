package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;


public class PollDeletionUpdate extends Update {

    private final int groupID;
    private final int pollID;

    public PollDeletionUpdate(@JsonProperty("time-created") Date timeCreated,
                              @JsonProperty("creator") int creator,
                              @JsonProperty("group-id") int groupID,
                              @JsonProperty("id") int pollID) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.pollID = pollID;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
