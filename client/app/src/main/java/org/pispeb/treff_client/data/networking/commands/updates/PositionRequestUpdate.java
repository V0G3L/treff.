package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PositionRequestUpdate extends UpdateToSerialize {

    @JsonProperty("end-time")
    public final Date endTime;

    public PositionRequestUpdate(@JsonProperty("type") String type,
                                 @JsonProperty("time-created") Date date,
                                 @JsonProperty("creator") int creator,
                                 @JsonProperty("end-time") Date endTime) {
        super(UpdateType.POSITION_REQUEST.toString(), date, creator);
        this.endTime = endTime;
    }
}
