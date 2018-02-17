package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteEvent {

    public final int id;
    public final String title;
    public final int creator;
    @JsonProperty("time-start")
    public final Date timeStart;
    @JsonProperty("time-end")
    public final Date timeEnd;
    public final double latitude;
    public final double longitude;
    public final int[] participants;

    public CompleteEvent(int id, String title, int creator, Date timeStart,
                         Date timeEnd, double latitude, double longitude, int[] participants){
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.latitude = latitude;
        this.longitude = longitude;
        this.participants = participants;
    }
}
