package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class AddGroupMembersCommand extends AbstractCommand {

    private Request output;

    public AddGroupMembersCommand(int groupId, int[] members, String token) {
        super(Response.class);
        output = new Request(groupId, members, token);
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

        @JsonProperty("group-id")
        public final int groupId;
        public final int[] members;
        public final String token;

        public Request(int groupId, int[] members, String token) {
            super("add-group-members");
            this.groupId = groupId;
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
