package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.PollCreateDescription;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class CreatePollCommand extends AbstractCommand{

    private Request output;

    public CreatePollCommand(int groupId, String question, boolean isMultiChoice,
                             Date timeVoteClose, String token) {
        super(Response.class);
        output = new Request(groupId, question, isMultiChoice, timeVoteClose, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        //TODO handle response

    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final PollCreateDescription poll;
        public final String token;

        public Request(int groupId, String question, boolean isMultiChoice,
                       Date timeVoteClose, String token) {
            super(CmdDesc.CREATE_POLL.toString());
            this.groupId = groupId;
            poll = new PollCreateDescription("type", question, isMultiChoice,
                    timeVoteClose);
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int id;

        public Response(@JsonProperty("id")int id) {
            this.id = id;
        }
    }
}
