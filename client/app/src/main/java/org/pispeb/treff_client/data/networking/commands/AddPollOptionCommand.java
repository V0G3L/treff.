package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.PollOptionCreateDescription;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class AddPollOptionCommand extends AbstractCommand {

    private Request output;

    public AddPollOptionCommand(int groupId, int pollId, long latitude, long longitude,
                                Date timeStart, Date timeEnd, String token) {
        super(Response.class);
        output = new Request(groupId, pollId, latitude, longitude, timeStart, timeEnd, token);
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
        public final PollOptionCreateDescription pollOption;
        public final String token;

        public Request(int groupId, int pollId, long latitude, long longitude,
                       Date timeStart, Date timeEnd, String token) {
            super("add-poll-option");
            this.groupId = groupId;
            this.pollId = pollId;
            pollOption = new PollOptionCreateDescription(latitude, longitude, timeStart, timeEnd);
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {
        public final int id;

        public Response(int id) {
            this.id = id;
        }
    }
}
