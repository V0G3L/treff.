package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.EventEditDescription;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class EditEventCommand extends AbstractCommand {

    private Request output;

    public EditEventCommand(int groupId, String title, int creatorId, Date timeStart,
                            Date timeEnd, Position position, int eventId, String token) {
        super(Response.class);
        output = new Request(groupId, title, creatorId, timeStart,
                timeEnd, position, eventId, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final EventEditDescription event;
        public final String token;

        public Request(int groupId, String title, int creatorId, Date timeStart,
                       Date timeEnd, Position position, int eventId, String token) {
            super("edit-event");
            this.groupId = groupId;
            event = new EventEditDescription(title, creatorId, timeStart, timeEnd, position, eventId);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
