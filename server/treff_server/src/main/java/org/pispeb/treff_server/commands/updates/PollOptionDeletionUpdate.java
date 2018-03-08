package org.pispeb.treff_server.commands.updates;

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

    public PollOptionDeletionUpdate(Date date, int creator, int groupID,
                                    int pollID, int pollOptionID) {
        super(UpdateType.POLL_OPTION_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOptionID = pollOptionID;
    }
}
