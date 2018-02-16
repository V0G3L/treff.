package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class SendChatMessageCommand extends AbstractCommand {

    private Request output;

    public SendChatMessageCommand(int groupId, String message, String token) {
        super(Response.class);
        output = new Request(groupId, message, token);
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
        public final String message;
        public final String token;

        public Request(int groupId, String message, String token) {
            super("send-chat-message");
            this.groupId = groupId;
            this.message = message;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
