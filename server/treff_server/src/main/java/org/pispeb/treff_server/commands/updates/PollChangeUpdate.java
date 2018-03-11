package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treff_server.interfaces.Poll;

import java.util.Date;

public class PollChangeUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("poll")
    @JsonSerialize(using = PollCompleteSerializer.class)
    public final Poll poll;

    public PollChangeUpdate(Date date, int creator, int groupID, Poll poll) {
        super(UpdateType.POLL_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.poll = poll;
    }
}
