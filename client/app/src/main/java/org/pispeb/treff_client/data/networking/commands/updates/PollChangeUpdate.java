package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompletePoll;

import java.util.Date;

public class PollChangeUpdate extends UpdateToSerialize {

    public final int groupID;
    public final CompletePoll poll;

    public PollChangeUpdate(@JsonProperty("type") String type,
                            @JsonProperty("time-created") Date date,
                            @JsonProperty("creator") int creator,
                            @JsonProperty("group-id") int groupID,
                            @JsonProperty("poll") CompletePoll poll) {
        super(UpdateType.POLL_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.poll = poll;
    }
}
