package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class PollDeletionUpdate extends UpdateToSerialize {

    public final int groupID;
    public final int pollID;

    public PollDeletionUpdate(@JsonProperty("type") String type,
                              @JsonProperty("time-created") Date date,
                              @JsonProperty("creator") int creator,
                              @JsonProperty("group-id") int groupID,
                              @JsonProperty("id") int pollID) {
        super(UpdateType.POLL_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
    }
}
