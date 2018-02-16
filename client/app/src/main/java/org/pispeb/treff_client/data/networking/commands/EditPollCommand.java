package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.PollEditDescription;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class EditPollCommand extends AbstractCommand {

    private Request output;

    public EditPollCommand(int groupId, String question, boolean isMultiChoice,
                             Date timeVoteClose, int id, String token) {
        super(Response.class);
        output = new Request(groupId, question, isMultiChoice, timeVoteClose, id, token);
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
        public final PollEditDescription poll;
        public final String token;

        public Request(int groupId, String question, boolean isMultiChoice,
                       Date timeVoteClose, int id, String token) {
            super("edit-poll");
            this.groupId = groupId;
            poll = new PollEditDescription(question, isMultiChoice, timeVoteClose, id);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {

        }
    }
}
