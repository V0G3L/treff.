package org.pispeb.treff_client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions
        .EventEditDescription;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;
import org.pispeb.treff_client.data.repositories.EventRepository;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class EditEventCommand extends AbstractCommand {

    private EventRepository eventRepository;
    private Request output;

    public EditEventCommand(int groupId, String title, int creatorId,
                            Date timeStart, Date timeEnd, Position position,
                            int eventId, String token,
                            EventRepository eventRepository) {
        super(Response.class);
        output = new Request(groupId, title, creatorId, timeStart,
                timeEnd, position, eventId, token);
        this.eventRepository = eventRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse; //empty
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLongitude(output.event.position.longitude);
        l.setLatitude(output.event.position.latitude);
        eventRepository.updateEvent(new Event(
                output.event.id,
                output.event.title,
                output.event.timeStart,
                output.event.timeEnd,
                l,
                output.groupId,
                output.event.creatorID));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final EventEditDescription event;
        public final String token;

        public Request(int groupId, String title, int creatorId, Date timeStart,
                       Date timeEnd, Position position, int eventId,
                       String token) {
            super("edit-event");
            this.groupId = groupId;
            event = new EventEditDescription(title, creatorId, timeStart,
                    timeEnd, position, eventId);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
