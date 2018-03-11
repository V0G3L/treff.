package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PositionRequestUpdate extends UpdateToSerialize {

    @JsonProperty("end-time")
    public final Date endTime;

    public PositionRequestUpdate(Date date, int creator, Date endTime) {
        super(UpdateType.POSITION_REQUEST.toString(), date, creator);
        this.endTime = endTime;
    }
}
