package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.UsergroupEditDescription;


import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class EditGroupCommand extends AbstractCommand {

    private final UserGroupRepository userGroupRepository;
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
        userGroupRepository.updateGroup(new UserGroup(
                output.group.id,
                output.group.name));
    }

    public static class Request extends AbstractRequest {


        public final UsergroupEditDescription group;
        public final String token;

        public Request(int groupId, String name, String token) {
            super(CmdDesc.EDIT_GROUP.toString());
            group = new UsergroupEditDescription("type", groupId, name);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
