package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON register object
 */

public class RegisterCommand extends AbstractCommand{

    private Request output;

    public RegisterCommand(String user, String pass) {
        super(Response.class);
        output = new Request(user, pass);
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String pass;

        public Request(String user, String pass) {
            super("register");
            this.user = user;
            this.pass = pass;
        }
    }


    public static class Response extends AbstractResponse {

        public final String token;

        public Response(String token) {
            this.token = token;
        }
    }

}
