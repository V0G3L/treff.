package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class PublishPositionCommand extends AbstractCommand {

    private Request output;

    public PublishPositionCommand(int groupId, Date timeEnd, String token) {
        super(Response.class);
        output = new Request(groupId, timeEnd, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO Handle response
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        @JsonProperty("time-end")
        public final Date timeEnd;
        public final String token;

        public Request(int groupId, Date timeEnd, String token) {
            super("publish-position");
            this.groupId = groupId;
            this.timeEnd = timeEnd;
            this.token = token;
        }
    }


    //Server returns empty object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
