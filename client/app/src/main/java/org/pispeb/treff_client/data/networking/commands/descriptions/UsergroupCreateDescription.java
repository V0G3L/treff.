package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable complete description of a user group as specified in the
 * treffpunkt protocol document, lacking the id property and the event and poll
 * arrays.
 * <p>
 * User group option create commands should require an instance of this class as
 * input.
 */
public class UsergroupCreateDescription {

    @JsonProperty("type")
    public final String type;
    @JsonProperty("name")
    public final String name;
    @JsonProperty("members")
    public final int[] memberIDs;

    public UsergroupCreateDescription(String type, String name, int[] memberIDs) {
        this.type = type;
        this.name = name;
        this.memberIDs = memberIDs;
    }


    public boolean equals(UsergroupCreateDescription usergroupCreateDescription) {
        return (this.name.equals(usergroupCreateDescription.name)
                && this.memberIDs.equals(usergroupCreateDescription.memberIDs));
    }
}
