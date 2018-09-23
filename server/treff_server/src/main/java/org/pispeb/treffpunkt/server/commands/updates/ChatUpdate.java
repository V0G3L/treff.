package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

public class ChatUpdate extends UpdateToSerialize {

    public final int groupId;
    public final String message;

    public ChatUpdate(Date date, int creator,
                             int groupId, String message) {
        super(UpdateType.CHAT.toString(), date, creator);
        this.groupId = groupId;
        this.message = message;
    }
}
