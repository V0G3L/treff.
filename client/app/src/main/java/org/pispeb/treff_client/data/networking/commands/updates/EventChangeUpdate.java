package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteEvent;
import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class EventChangeUpdate extends Update {

    private final int groupID;
    private final CompleteEvent event;

    public EventChangeUpdate(@JsonProperty("time-created") Date timeCreated,
                             @JsonProperty("creator") int creator,
                             @JsonProperty("group-id") int groupID,
                             @JsonProperty("event") CompleteEvent event) {
        super(timeCreated, creator);
        this.groupID = groupID;
        this.event = event;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        Event eventEntity
                = repositorySet.eventRepository.getEvent(this.event.id);

        if (eventEntity != null) { // existing event
            eventEntity.setName(event.title);
            eventEntity.setLocation(
                    toLocation(event.latitude, event.longitude));
            eventEntity.setStart(event.timeStart);
            eventEntity.setEnd(event.timeEnd);
            // TODO: set participants
            repositorySet.eventRepository.updateEvent(eventEntity);
        } else { // new event
            eventEntity = new Event(event.id, event.title, event.timeStart,
                    event.timeEnd, toLocation(event.latitude, event.longitude),
                    event.creator, groupID);
            repositorySet.eventRepository.addEvent(eventEntity);
        }

    }
}
