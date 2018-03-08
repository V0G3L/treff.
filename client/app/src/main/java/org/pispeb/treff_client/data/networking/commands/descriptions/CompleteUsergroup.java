package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteUsergroup {

    public final int id;
    public final String name;
    public final CompleteMembership[] members;
    public final int[] events;
    public final int[] polls;

    public CompleteUsergroup(@JsonProperty("type") String type,
                             @JsonProperty("id") int id,
                             @JsonProperty("name") String name,
                             @JsonProperty("members") CompleteMembership[]
                                     members,
                             @JsonProperty("events") int[] events,
                             @JsonProperty("polls") int[] polls){
        this.id = id;
        this.name = name;
        this.members = members;
        this.events = events;
        this.polls = polls;

    }
}
