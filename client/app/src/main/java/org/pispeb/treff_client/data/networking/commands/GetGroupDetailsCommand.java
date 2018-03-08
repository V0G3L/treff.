package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteUsergroup;


import org.pispeb.treff_client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 17.02.2018.
 */

public class GetGroupDetailsCommand extends AbstractCommand {

    private Request output;
    private UserGroupRepository userGroupRepository;

    public GetGroupDetailsCommand(int id, String token,
                                  UserGroupRepository userGroupRepository) {
        super(Response.class);
        output = new Request(id, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        UserGroup newGroup = new UserGroup(output.id, response.usergroup.name);
        if (userGroupRepository.getGroup(output.id) == null) {
            userGroupRepository.addGroup(newGroup);
        } else {
            userGroupRepository.updateGroup(
                    new UserGroup(output.id, response.usergroup.name));
        }
        userGroupRepository.updateGroupMembers(output.id, response.usergroup
                .members);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.GET_GROUP_DETAILS.toString());
            this.id = id;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompleteUsergroup usergroup;

        public Response(@JsonProperty("group")
                        CompleteUsergroup usergroup) {
            this.usergroup = usergroup;
        }
    }
}
