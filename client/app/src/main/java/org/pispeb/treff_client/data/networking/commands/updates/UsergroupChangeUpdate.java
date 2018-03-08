package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteUsergroup;


import java.util.Date;

public class UsergroupChangeUpdate extends UpdateToSerialize{
    @JsonProperty("usergroup")
    public final CompleteUsergroup usergroup;

    public UsergroupChangeUpdate(@JsonProperty("type") String type,
                                 @JsonProperty("time-created") Date date,
                                 @JsonProperty("creator") int creator,
                                 @JsonProperty("usergroup") CompleteUsergroup group) {
        super(UpdateType.USERGROUP_CHANGE.toString(), date, creator);
        this.usergroup = group;
    }
}
