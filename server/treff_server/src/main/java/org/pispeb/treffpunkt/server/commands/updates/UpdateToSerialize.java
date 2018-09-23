package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

public class UpdateToSerialize {
    public final String type;
    public final Date date;
    public final int creator;

    public UpdateToSerialize(String type, Date date, int creator) {
        this.type = type;
        this.date = date;
        this.creator = creator;
    }
}
