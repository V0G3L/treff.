package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteAccount;

/**
 * Created by matth on 17.02.2018.
 */

public class GetUserDetailsCommand extends AbstractCommand {
    private Request output;

    public GetUserDetailsCommand(int id, String token) {
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
        //TODO response
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super("get-user-details");
            this.id = id;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompleteAccount account;

        public Response(CompleteAccount account) {
            this.account = account;
        }
    }
}
