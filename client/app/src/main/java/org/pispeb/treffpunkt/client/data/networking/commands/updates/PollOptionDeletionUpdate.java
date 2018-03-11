package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

/**
 * @author tim
 */
public class PollOptionDeletionUpdate extends Update {

    private final int groupID;
    private final int pollID;
    private final int pollOptionID;

    public PollOptionDeletionUpdate(@JsonProperty("time-created")
                                            Date timeCreated,
                                    @JsonProperty("creator") int creator,
                                    @JsonProperty("group-id") int groupID,
                                    @JsonProperty("poll-id") int pollID,
                                    @JsonProperty("id") int pollOptionID) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOptionID = pollOptionID;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
