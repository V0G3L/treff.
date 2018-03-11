package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class PublishPositionCommand extends AbstractCommand {

    private Request output;
    private final UserGroupRepository userGroupRepository;

    public PublishPositionCommand(int groupId, Date timeEnd, String token,
                                  UserGroupRepository userGroupRepository) {
        super(Response.class);
        output = new Request(groupId, timeEnd, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        // if the time End is before current time, stop sending
        userGroupRepository.setIsSharing(output.groupId,
                output.timeEnd.after(new Date()));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        @JsonProperty("time-end")
        public final Date timeEnd;
        public final String token;

        public Request(int groupId, Date timeEnd, String token) {
            super(CmdDesc.PUBLISH_POSITION.toString());
            this.groupId = groupId;
            this.timeEnd = timeEnd;
            this.token = token;
        }
    }

    //Server returns empty object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
