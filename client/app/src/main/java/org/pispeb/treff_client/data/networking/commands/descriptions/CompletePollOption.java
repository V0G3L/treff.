package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 08.03.2018.
 */

public class CompletePollOption {

    public final String type;
    public final int id;
    public final double latitude;
    public final double longitude;
    public final Date timeStart;
    public final Date timeEnd;
    public final int[] supporters;

    public CompletePollOption(@JsonProperty("type") String type,
                              @JsonProperty("id") int id,
                              @JsonProperty("latitude") double latitude,
                              @JsonProperty("longitude") double longitude,
                              @JsonProperty("time-start") Date timeStart,
                              @JsonProperty("time-end") Date timeEnd,
                              @JsonProperty("supporters") int[] supporters) {

        this.type = "poll option";
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.supporters = supporters;
    }

}
