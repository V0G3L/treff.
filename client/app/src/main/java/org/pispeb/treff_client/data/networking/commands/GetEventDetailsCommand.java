package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteEvent;

/**
 * Created by matth on 17.02.2018.
 */

public class GetEventDetailsCommand extends AbstractCommand {

    private Request output;

    public GetEventDetailsCommand(int id, int groupId, String token) {
        super(Response.class);
        output = new Request(id, groupId, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        //TODO response
    }

    public static class Request extends AbstractRequest {

        public final int id;
        @JsonProperty("group-id")
        public final int groupId;
        public final String token;

        public Request(int id, int groupId, String token) {
            super("get-event-details");
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
