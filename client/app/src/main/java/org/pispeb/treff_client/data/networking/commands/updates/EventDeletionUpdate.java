package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

/**
 * @author tim
 */
public class EventDeletionUpdate extends Update {

    private final int groupID;
    private final int eventID;

    public EventDeletionUpdate(@JsonProperty("time-created") Date timeCreated,
                               @JsonProperty("creator") int creator,
                               @JsonProperty("group-id") int groupID,
                               @JsonProperty("id") int eventID) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.eventID = eventID;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        repositorySet.eventRepository.deleteEvent(eventID);
    }
}
