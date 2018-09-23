package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

/**
 * @author tim
 */
public class PollDeletionUpdate extends UpdateToSerialize {

    public final int groupID;
    public final int pollID;

    public PollDeletionUpdate(Date date, int creator, int groupID,
                              int pollID) {
        super(UpdateType.POLL_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
    }
}
