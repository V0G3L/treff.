package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a  JSON get-user-id object
 */

public class GetUserIdCommand extends AbstractCommand {

    private Request output;

    public GetUserIdCommand(String user, String token) {
        super(Response.class);
        output = new Request(user, token);
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onResponse(AbstractResponse response) {

    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String token;

        public Request(String user, String token) {
            super("get-user-id");
            this.user = user;
            this.token = token;
        }
    }

    public static class Response extends AbstractResponse {
        public final int id;

        public Response(int id) {
            this.id = id;
        }
    }


}
