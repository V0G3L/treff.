package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author tim
 */
public class PollOptionDeletionUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("poll-id")
    public final int pollID;
    @JsonProperty("id")
    public final int pollOptionID;

    public PollOptionDeletionUpdate(@JsonProperty("type") String type,
                                    @JsonProperty("time-created") Date date,
                                    @JsonProperty("creator") int creator,
                                    @JsonProperty("group-id") int groupID,
                                    @JsonProperty("poll-id") int pollID,
                                    @JsonProperty("id") int pollOptionID) {
        super(UpdateType.POLL_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOptionID = pollOptionID;
    }
}
