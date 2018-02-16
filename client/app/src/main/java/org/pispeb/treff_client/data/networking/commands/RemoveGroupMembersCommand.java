package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class RemoveGroupMembersCommand extends AbstractCommand {

    private Request output;

    public RemoveGroupMembersCommand(int id, int[] members, String token) {
        super(Response.class);
        output = new Request(id, members, token);
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
        public final int[] members;
        public final String token;

        public Request(int id, int[] members, String token) {
            super("remove-group-members");
            this.id = id;
            this.members = members;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
