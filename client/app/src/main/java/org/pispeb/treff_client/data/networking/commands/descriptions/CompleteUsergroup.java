package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteUsergroup {

    public final String type;
    public final int id;
    public final String name;
    // No longer memberships!
    public final int[] members;
    public final int[] events;
    public final int[] polls;

    // No longer memberships!
    public CompleteUsergroup(@JsonProperty("type") String type,
                             @JsonProperty("id") int id,
                             @JsonProperty("name") String name,
                             @JsonProperty("members") int[] members,
                             @JsonProperty("events") int[] events,
                             @JsonProperty("polls") int[] polls){
        this.type = type;
        this.id = id;
        this.name = name;
        this.members = members;
        this.events = events;
        this.polls = polls;

    }
}
