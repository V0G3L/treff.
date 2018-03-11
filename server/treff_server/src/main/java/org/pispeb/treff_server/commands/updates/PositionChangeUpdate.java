package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Position;

import java.util.Date;

public class PositionChangeUpdate extends UpdateToSerialize {

    @JsonProperty("latitude")
    public final double latitude;
    @JsonProperty("longitude")
    public final double longitude;
    @JsonProperty("time-measured")
    public final Date timeMeasured;

    public PositionChangeUpdate(Date date, int creator,
                                Position position, Date timeMeasured) {
        super(UpdateType.POSITION_CHANGE.toString(), date, creator);
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.timeMeasured = timeMeasured;
    }
}
