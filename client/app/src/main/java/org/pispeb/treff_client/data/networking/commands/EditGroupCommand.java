package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.UsergroupEditDescription;


import org.pispeb.treff_client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class EditGroupCommand extends AbstractCommand {

    private UserGroupRepository userGroupRepository;
    private Request output;

    public EditGroupCommand(int groupId, String name, String token,
                            UserGroupRepository userGroupRepository) {
        super(Response.class);
        output = new Request(groupId, name, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userGroupRepository.update(new UserGroup(output.group.name, null,
                null));
    }

    public static class Request extends AbstractRequest {


        public final UsergroupEditDescription group;
        public final String token;

        public Request(int groupId, String name, String token) {
            super("edit-group");
            group = new UsergroupEditDescription(groupId, name);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
