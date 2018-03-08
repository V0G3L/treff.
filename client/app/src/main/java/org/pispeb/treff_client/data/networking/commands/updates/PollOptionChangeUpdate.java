package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompletePollOption;

import java.util.Date;

public class PollOptionChangeUpdate extends UpdateToSerialize{

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("poll-id")
    public final int pollID;
    @JsonProperty("poll-option")
    public final CompletePollOption pollOption;

    public PollOptionChangeUpdate(@JsonProperty("type") String type,
                                  @JsonProperty("time-created") Date date,
                                  @JsonProperty("creator") int creator,
                                  @JsonProperty("group-id") int groupID,
                                  @JsonProperty("poll-id") int pollID,
                                  @JsonProperty("poll-option") CompletePollOption pollOption) {
        super(UpdateType.POLL_OPTION_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOption = pollOption;
    }
}
