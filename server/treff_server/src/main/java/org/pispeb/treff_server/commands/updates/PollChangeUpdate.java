package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class PollChangeUpdate extends UpdateToSerialize {
    @JsonProperty("poll")
    @JsonSerialize(using = PollCompleteSerializer.class)
    public final Poll poll;

    public PollChangeUpdate(Date date, int creator, Poll poll) {
        super(Update.UpdateType.POLL_CHANGE.toString(), date, creator);
        this.poll = poll;
    }
}
