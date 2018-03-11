package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompletePollOption;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class PollOptionChangeUpdate extends Update {

    private final int groupID;
    private final int pollID;
    private final CompletePollOption pollOption;

    public PollOptionChangeUpdate(@JsonProperty("time-created")
                                          Date timeCreated,
                                  @JsonProperty("creator") int creator,
                                  @JsonProperty("group-id") int groupID,
                                  @JsonProperty("poll-id") int pollID,
                                  @JsonProperty("poll-option")
                                          CompletePollOption pollOption) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOption = pollOption;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
