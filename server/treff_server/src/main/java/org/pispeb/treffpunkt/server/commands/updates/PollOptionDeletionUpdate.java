package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

/**
 * @author tim
 */
public class PollOptionDeletionUpdate extends UpdateToSerialize {

    public final int groupID;
    public final int pollID;
    public final int pollOptionID;

    public PollOptionDeletionUpdate(Date date, int creator, int groupID,
                                    int pollID, int pollOptionID) {
        super(UpdateType.POLL_OPTION_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.pollID = pollID;
        this.pollOptionID = pollOptionID;
    }
}
