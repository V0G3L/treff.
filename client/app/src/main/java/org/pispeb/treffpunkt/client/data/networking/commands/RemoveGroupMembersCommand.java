package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class RemoveGroupMembersCommand extends AbstractCommand {

    private final UserGroupRepository userGroupRepository;
    private Request output;

    public RemoveGroupMembersCommand(int id, int[] members, String token,
                                     UserGroupRepository userGroupRepository) {
        super(Response.class);
        output = new Request(id, members, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userGroupRepository.removeGroupMembers(output.id, output.members);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final int[] members;
        public final String token;

        public Request(int id, int[] members, String token) {
            super(CmdDesc.REMOVE_GROUP_MEMBERS.toString());
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
