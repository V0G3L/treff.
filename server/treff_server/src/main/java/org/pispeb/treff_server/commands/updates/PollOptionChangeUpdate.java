package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers
        .PollOptionCompleteSerializer;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class PollOptionChangeUpdate extends UpdateToSerialize{

    @JsonProperty("poll-option")
    @JsonSerialize(using = PollOptionCompleteSerializer.class)
    public final PollOption pollOption;

    public PollOptionChangeUpdate(Date date, int creator, PollOption pollOption) {
        super(Update.UpdateType.POLL_OPTION_CHANGE.toString(), date, creator);
        this.pollOption = pollOption;
    }
}
