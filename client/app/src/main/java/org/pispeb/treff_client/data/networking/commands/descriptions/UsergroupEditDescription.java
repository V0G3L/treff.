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

    public final int id;
    public final String name;

    public UsergroupEditDescription(@JsonProperty("id") int id,
                                    @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
