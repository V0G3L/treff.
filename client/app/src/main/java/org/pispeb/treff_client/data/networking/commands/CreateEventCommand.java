package org.pispeb.treff_client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions
        .EventCreateDescription;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;
import org.pispeb.treff_client.data.repositories.EventRepository;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class CreateEventCommand extends AbstractCommand {

    private final EventRepository eventRepository;
    private Request output;

    public CreateEventCommand(int groupId, String title, int creatorId,
                              Date timeStart, Date timeEnd, Location location,
                              String token, EventRepository eventRepository) {
        super(Response.class);
        output = new Request(groupId, title, creatorId, timeStart, timeEnd,
                location, token);
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
        l.setLatitude(output.event.latitude);
        l.setLongitude(output.event.longitude);
        eventRepository.addEvent(new Event(
                response.id,
                output.event.title,
                output.event.timeStart,
                output.event.timeEnd,
                l,
                output.event.creatorID,
                output.groupId));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final EventCreateDescription event;
        public final String token;

        public Request(int groupId, String title, int creatorId, Date timeStart,
                       Date timeEnd, Location location, String token) {
            super(CmdDesc.CREATE_EVENT.toString());
            this.groupId = groupId;
            event = new EventCreateDescription(title, creatorId, timeStart,
                    timeEnd, location);
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int id;

        public Response(@JsonProperty("id")int id) {
            this.id = id;
        }
    }
}
