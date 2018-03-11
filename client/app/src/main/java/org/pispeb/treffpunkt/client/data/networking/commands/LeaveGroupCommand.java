package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

public class LeaveGroupCommand extends AbstractCommand {

    private final UserGroupRepository userGroupRepository;
    private final Request output;
    private final int groupID;

    public LeaveGroupCommand(int groupID, String token,
                             UserGroupRepository userGroupRepository) {
        super(Response.class);
        this.groupID = groupID;
        output = new Request(groupID, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        // delete local records
        userGroupRepository.removeGroup(userGroupRepository.getGroup(groupID));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("id")
        public final int groupID;
        @JsonProperty("token")
        public final String token;

        public Request(int groupID, String token) {
            super(CmdDesc.LEAVE_GROUP.toString());
            this.groupID = groupID;
            this.token = token;
        }
    }

    public static class Response extends AbstractResponse { }
}
