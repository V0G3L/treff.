package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class PositionRequestUpdate extends UpdateToSerialize {

    @JsonProperty("event")
    Date endTime;

    public PositionRequestUpdate(Date date, int creator, Date endTime) {
        super(Update.UpdateType.POSITION_REQUEST.toString(), date, creator);
        this.endTime = endTime;
    }
}
