package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Poll;

import java.util.Date;

public class PollChangeUpdate extends UpdateToSerialize {

    public final int groupID;
    @JsonSerialize(using = PollCompleteSerializer.class)
    public final Poll poll;

    public PollChangeUpdate(Date date, int creator, int groupID, Poll poll) {
        super(UpdateType.POLL_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.poll = poll;
    }
}
