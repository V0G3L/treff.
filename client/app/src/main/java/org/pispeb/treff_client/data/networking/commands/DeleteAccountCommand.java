package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON delete-account object
 */

public class DeleteAccountCommand extends AbstractCommand{

    private Request output;

    public DeleteAccountCommand(String pass, String token) {
        super(Response.class);
        output = new Request(pass, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO logout
    }

    public static class Request extends AbstractRequest {

        public final String pass;
        public final String token;

        public Request(String pass, String token) {
            super("delete-account");
            this.pass = pass;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
