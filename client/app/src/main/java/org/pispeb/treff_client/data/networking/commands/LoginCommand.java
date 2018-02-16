package org.pispeb.treff_client.data.networking.commands;

import android.util.Log;

/**
 * Class representing a JSON login object
 */

public class LoginCommand extends AbstractCommand{

    private Request output;

    public LoginCommand(String user, String pass) {
        super(Response.class);
        output = new Request(user, pass);
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

        public final String user;
        public final String pass;

        public Request(String user, String pass) {
            super("login");
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
