package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class RemovePollCommand extends AbstractCommand {

    private Request output;

    public RemovePollCommand(int groupId, int pollId, String token) {
        super(Response.class);
        output = new Request(groupId, pollId, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        //TODO handle response

    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final int id;
        public final String token;

        public Request(int groupId, int id, String token) {
            super(CmdDesc.REMOVE_POLL.toString());
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
