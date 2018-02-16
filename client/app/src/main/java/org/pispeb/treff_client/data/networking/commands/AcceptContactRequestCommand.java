package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON accept-contact-request object
 */

public class AcceptContactRequestCommand extends AbstractCommand{


    private Request output;

    public AcceptContactRequestCommand(int id, String token) {
        super(Response.class);
        output = new Request(id, token);
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

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super("accept-contact-request");
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
