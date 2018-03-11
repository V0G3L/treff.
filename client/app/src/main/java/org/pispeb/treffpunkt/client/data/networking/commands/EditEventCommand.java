package org.pispeb.treffpunkt.client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions
        .EventEditDescription;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class EditEventCommand extends AbstractCommand {

    private final EventRepository eventRepository;
    private Request output;

    public EditEventCommand(int groupId, String title, int creatorId,
                            Date timeStart, Date timeEnd, Location location,
                            int eventId, String token,
                            EventRepository eventRepository) {
        super(Response.class);
        output = new Request(groupId, title, creatorId, timeStart,
                timeEnd, location, eventId, token);
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
        l.setLatitude(output.event.latitude);
        l.setLongitude(output.event.longitude);
        eventRepository.updateEvent(new Event(
                output.event.id,
                output.event.title,
                output.event.timeStart,
                output.event.timeEnd,
                l,
                output.event.creatorID,
                output.groupId
                ));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final EventEditDescription event;
        public final String token;

        public Request(int groupId, String title, int creatorId, Date timeStart,
                       Date timeEnd, Location location, int eventId,
                       String token) {
            super(CmdDesc.EDIT_EVENT.toString());
            this.groupId = groupId;
            event = new EventEditDescription(title, creatorId, timeStart,
                    timeEnd, location, eventId);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
