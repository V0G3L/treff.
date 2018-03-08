package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class ShallowUserGroup {

    public final int id;
    public final String checksum;

    public ShallowUserGroup(@JsonProperty("type") String type,
                            @JsonProperty("id") int id,
                            @JsonProperty("checksum") String checksum) {
        this.id = id;
        this.checksum = checksum;
    }
}
