package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers
        .PollOptionCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.PollOption;

import java.util.Date;

public class PollOptionChangeUpdate extends UpdateToSerialize{

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("poll-id")
    public final int pollID;
    @JsonProperty("poll-option")
    @JsonSerialize(using = PollOptionCompleteSerializer.class)
    public final PollOption pollOption;

    public PollOptionChangeUpdate(Date date, int creator, int groupID,
                                  int pollID, PollOption pollOption) {
        super(UpdateType.POLL_OPTION_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOption = pollOption;
    }
}
