package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.PollOptionCreateDescription;
import org.pispeb.treff_client.data.networking.commands.descriptions.PollOptionEditDescription;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class EditPollOptionCommand extends AbstractCommand {

    private Request output;

    public EditPollOptionCommand(int groupId, int pollId, long latitude, long longitude,
                                 Date timeStart, Date timeEnd, int optionId, String token) {
        super(Response.class);
        output = new Request(groupId, pollId, latitude, longitude,
                timeStart, timeEnd, optionId, token);
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
        @JsonProperty("poll-id")
        public final int pollId;
        @JsonProperty("poll-option")
        public final PollOptionEditDescription pollOption;
        public final String token;

        public Request(int groupId, int pollId, long latitude, long longitude,
                       Date timeStart, Date timeEnd, int id, String token) {
            super(CmdDesc.EDIT_POLLOPTION.toString());
            this.groupId = groupId;
            this.pollId = pollId;
            pollOption = new PollOptionEditDescription(latitude, longitude, timeStart, timeEnd, id);
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {

        }
    }
}
