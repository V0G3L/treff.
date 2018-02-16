package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.UsergroupCreateDescription;

/**
 * Created by matth on 16.02.2018.
 */

public class CreateGroupCommand extends AbstractCommand {

    private Request output;

    public CreateGroupCommand(String name, int[] members, String token) {
        super(Response.class);
        output = new Request(name, members, token);
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

        public final UsergroupCreateDescription group;
        public final String token;

        public Request(String name, int[] members, String token) {
            super("create-group");
            group = new UsergroupCreateDescription(name, members);
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
