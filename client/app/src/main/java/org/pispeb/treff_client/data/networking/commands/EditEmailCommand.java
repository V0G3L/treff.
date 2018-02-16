package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON edit-email object
 */

public class EditEmailCommand extends AbstractCommand{

    private Request output;

    public EditEmailCommand(String email, String token) {
        super(Response.class);
        output = new Request(email, token);
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

        public final String email;
        public final String token;

        public Request(String email, String token) {
            super("edit-email");
            this.email = email;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}