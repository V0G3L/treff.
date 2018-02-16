package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON edit-username object
 */

public class EditUsernameCommand extends AbstractCommand{

    private Request output;

    public EditUsernameCommand(String username, String token) {
        super(Response.class);
        output = new Request(username, token);
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

        public final String username;
        public final String token;

        public Request(String username, String token) {
            super("edit-username");
            this.username = username;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
