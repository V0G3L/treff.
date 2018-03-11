package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author tim
 */
public class PollDeletionUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("id")
    public final int pollID;

    public PollDeletionUpdate(Date date, int creator, int groupID,
                              int pollID) {
        super(UpdateType.POLL_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
    }
}
