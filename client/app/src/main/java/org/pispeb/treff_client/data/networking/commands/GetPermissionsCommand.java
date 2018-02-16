package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class GetPermissionsCommand extends AbstractCommand {

    private Request output;

    public GetPermissionsCommand(int id, int userId, String token) {
        super(Response.class);
        output = new Request(id, userId, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO handle response
    }

    public static class Request extends AbstractRequest {

        public final int id;

        @JsonProperty("user-id")
        public final int userId;

        public final String token;

        public Request(int id, int userId, String token) {
            super("get-permissions");
            this.id = id;
            this.userId = userId;
            this.token = token;
        }
    }

    //TODO define response
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
