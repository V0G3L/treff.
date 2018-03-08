package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UpdatesWithoutSpecialParameters extends UpdateToSerialize{

    public UpdatesWithoutSpecialParameters(@JsonProperty("type") String type,
                                           @JsonProperty("time-created") Date date,
                                           @JsonProperty("creator") int creator) {
        super(type.toString(), date, creator);
    }
}
