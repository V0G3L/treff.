package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UpdateToSerialize {
    @JsonProperty("type")
    String type;
    @JsonProperty("date")
    Date date;
    @JsonProperty("creator")
    int creator;

    public UpdateToSerialize(String type, Date date, int creator) {
        this.type = type;
        this.date = date;
        this.creator = creator;
    }
}
