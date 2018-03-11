package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.UsergroupCreateDescription;


import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class CreateGroupCommand extends AbstractCommand {

    private final UserGroupRepository userGroupRepository;
    private Request output;

    public CreateGroupCommand(String name, int[] members, String token,
                              UserGroupRepository userGroupRepository) {
        super(Response.class);
        output = new Request(name, members, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userGroupRepository.addGroup(new UserGroup(response.id, output.group.name));
        userGroupRepository.addGroupMembers(response.id, output.group.memberIDs);
    }

    public static class Request extends AbstractRequest {

        public final UsergroupCreateDescription group;
        public final String token;

        public Request(String name, int[] members, String token) {
            super(CmdDesc.CREATE_GROUP.toString());
            group = new UsergroupCreateDescription("type", name, members);
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int id;
        public Response(@JsonProperty("id") int id) {
            this.id = id;
        }
    }
}
