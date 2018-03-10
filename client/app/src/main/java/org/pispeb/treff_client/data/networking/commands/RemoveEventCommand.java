package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.EventRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class RemoveEventCommand extends AbstractCommand {

    private final EventRepository eventRepository;
    private Request output;

    public RemoveEventCommand(int groupId, int id, String token,
                              EventRepository eventRepository) {
        super(Response.class);
        output = new Request(groupId, id, token);
        this.eventRepository = eventRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        eventRepository.deleteEvent(output.id);
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final int id;
        public final String token;

        public Request(int groupId, int id, String token) {
            super(CmdDesc.REMOVE_EVENT.toString());
            this.groupId = groupId;
            this.id = id;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {

        }
    }
}
