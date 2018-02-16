package org.pispeb.treff_client.data.networking.commands;

/**
 * Created by matth on 16.02.2018.
 */

public class RemoveContactCommand extends AbstractCommand {

    private Request output;

    public RemoveContactCommand(int id, String token) {
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
            super("remove-contact");
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
