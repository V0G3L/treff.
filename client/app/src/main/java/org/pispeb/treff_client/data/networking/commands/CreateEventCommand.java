package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.EventCreateDescription;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class CreateEventCommand extends AbstractCommand {

    private Request output;

    public CreateEventCommand(int groupId, String title, int creatorId, Date timeStart,
                              Date timeEnd, Position position, String token) {
        super(Response.class);
        output = new Request(groupId, title, creatorId, timeStart, timeEnd, position, token);
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
        public final EventCreateDescription event;
        public final String token;

        public Request(int groupId, String title, int creatorId, Date timeStart,
                       Date timeEnd, Position position, String token) {
            super("create-event");
            this.groupId = groupId;
            event = new EventCreateDescription(title, creatorId, timeStart, timeEnd, position);
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int id;
        public Response(int id) {
            this.id = id;
        }
    }
}
