package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.networking.commands.descriptions.ShallowUserGroup;


import org.pispeb.treff_client.data.repositories.UserGroupRepository;


public class ListGroupsCommand extends AbstractCommand {

    private Request output;
    private final UserGroupRepository userGroupRepository;
    private final RequestEncoder encoder;

    public ListGroupsCommand(String token,
                            UserGroupRepository userGroupRepository,
                            RequestEncoder encoder) {
        super(Response.class);
        output = new Request(token);
        this.userGroupRepository = userGroupRepository;
        this.encoder = encoder;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // update groups that are not yet in the local database
        for (ShallowUserGroup g : response.groups) {
            if (userGroupRepository.getGroup(g.id) == null) {
                encoder.getGroupDetails(g.id);
            }
        }
    }

    public static class Request extends AbstractRequest {

        public final String token;

        public Request(String token) {
            super(CmdDesc.LIST_GROUPS.toString());
            this.token = token;
        }
    }

    public static class Response extends AbstractResponse {

        public final ShallowUserGroup[] groups;

        public Response(@JsonProperty("groups") ShallowUserGroup[] groups) {
            this.groups = groups;
        }
    }
}
