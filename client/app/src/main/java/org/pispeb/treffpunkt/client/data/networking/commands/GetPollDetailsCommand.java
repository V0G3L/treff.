package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompletePoll;

/**
 * Created by matth on 17.02.2018.
 */

public class GetPollDetailsCommand extends AbstractCommand {

    private Request output;

    public GetPollDetailsCommand(int id, int groupId, String token) {
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
            super(CmdDesc.GET_POLL_DETAILS.toString());
            this.id = id;
            this.groupId = groupId;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompletePoll poll;

        public Response(@JsonProperty("poll") CompletePoll poll) {
            this.poll = poll;
        }
    }
}
