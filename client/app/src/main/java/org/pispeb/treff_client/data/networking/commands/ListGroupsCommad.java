package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.ShallowUserGroup;

/**
 * Created by matth on 17.02.2018.
 */

public class ListGroupsCommad extends AbstractCommand {

    private Request output;

    public ListGroupsCommad(String token) {
        super(Response.class);
        output = new Request(token);
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

        public final String token;

        public Request(String token) {
            super("list-groups");
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {

        public final ShallowUserGroup[] groups;

        public Response(ShallowUserGroup[] groups) {
            this.groups = groups;
        }
    }
}
