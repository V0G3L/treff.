package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class RemovePollOptionCommand extends AbstractCommand {

    private Request output;

    public RemovePollOptionCommand(int groupId, int pollId, int id, String token) {
        super(Response.class);
        output = new Request(groupId, pollId, id, token);
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
        @JsonProperty("poll-id")
        public final int pollId;
        public final int id;
        public final String token;

        public Request(int groupId, int pollId, int id, String token) {
            super(CmdDesc.REMOVE_POLL_OPTION.toString());
            this.groupId = groupId;
            this.pollId = pollId;
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
