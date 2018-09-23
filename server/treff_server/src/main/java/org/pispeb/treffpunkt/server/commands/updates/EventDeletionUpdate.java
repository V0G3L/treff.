package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

/**
 * @author tim
 */
public class EventDeletionUpdate extends UpdateToSerialize {

    public final int groupID;
    public final int eventID;

    public EventDeletionUpdate(Date date, int creator, int groupID,
                               int eventID) {
        super(UpdateType.EVENT_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.eventID = eventID;
    }
}
