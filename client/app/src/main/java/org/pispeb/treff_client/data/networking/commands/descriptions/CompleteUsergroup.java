package org.pispeb.treff_client.data.networking.commands.descriptions;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteUsergroup {

    public final int id;
    public final String name;
    public final CompleteMembership[] members;
    public final int[] events;
    public final int[] polls;

    public CompleteUsergroup(int id, String name,
                             CompleteMembership[] members, int[] events, int[] polls){
        this.id = id;
        this.name = name;
        this.members = members;
        this.events = events;
        this.polls = polls;

    }
}
