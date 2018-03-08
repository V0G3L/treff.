package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable complete description of a user group as specified in the
 * treffpunkt protocol document, lacking the member, event and poll arrays.
 * <p>
 * User group edit create commands should require an instance of this class as
 * input.
 */
public class UsergroupEditDescription {

    @JsonProperty("type")
    public final String type;
    @JsonProperty("id")
    public final int id;
    @JsonProperty("name")
    public final String name;

    public UsergroupEditDescription(String type, int id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
    }

    public boolean equals(UsergroupEditDescription usergroupEditDescription) {
        return (this.id == usergroupEditDescription.id
                && this.name.equals(usergroupEditDescription.name));
    }
}