package org.pispeb.treff_client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions
        .CompleteEvent;


import org.pispeb.treff_client.data.repositories.EventRepository;

/**
 * Created by matth on 17.02.2018.
 */

public class GetEventDetailsCommand extends AbstractCommand {

    private Request output;
    private EventRepository eventRepository;

    public GetEventDetailsCommand(int id, int groupId, String token,
                                  EventRepository eventRepository) {
        super(Response.class);
        output = new Request(id, groupId, token);
        this.eventRepository = eventRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(response.event.latitude);
        l.setLongitude(response.event.longitude);
        Event event = new Event(
                output.id,
                response.event.title,
                response.event.timeStart,
                response.event.timeEnd,
                l,
                response.event.creator,
                output.groupId);
        if (eventRepository.getEvent(output.id) == null) {
            eventRepository.addEvent(event);
        } else {
            eventRepository.updateEvent(event);
        }
    }

    public static class Request extends AbstractRequest {

        public final int id;
        @JsonProperty("group-id")
        public final int groupId;
        public final String token;

        public Request(int id, int groupId, String token) {
            super(CmdDesc.GET_EVENT_DETAILS.toString());
            this.id = id;
            this.groupId = groupId;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompleteEvent event;

        public Response(@JsonProperty("event") CompleteEvent event) {
            this.event = event;
        }
    }
}
