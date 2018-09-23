package org.pispeb.treffpunkt.server.commands.updates;

import org.pispeb.treffpunkt.server.service.domain.Position;

import java.util.Date;

public class PositionChangeUpdate extends UpdateToSerialize {

    public final Position position;

    public PositionChangeUpdate(Date date, int creator,
                                Position position) {
        super(UpdateType.POSITION_CHANGE.toString(), date, creator);
        this.position = position;
    }
}
