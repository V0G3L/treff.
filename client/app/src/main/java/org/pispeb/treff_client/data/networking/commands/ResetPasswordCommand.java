package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON reset-password object
 */

public class ResetPasswordCommand extends AbstractCommand{

    private Request output;

    public ResetPasswordCommand(String email) {
        super(Response.class);
        output = new Request(email);
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

        public final String email;

        public Request(String email) {
            super("reset-password");
            this.email = email;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
