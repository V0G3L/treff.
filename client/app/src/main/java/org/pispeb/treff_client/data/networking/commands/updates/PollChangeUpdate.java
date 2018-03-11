package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompletePoll;
import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class PollChangeUpdate extends Update {

    private final int groupID;
    private final CompletePoll poll;

    public PollChangeUpdate(@JsonProperty("time-created") Date timeCreated,
                            @JsonProperty("creator") int creator,
                            @JsonProperty("group-id") int groupID,
                            @JsonProperty("poll") CompletePoll poll) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.poll = poll;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
