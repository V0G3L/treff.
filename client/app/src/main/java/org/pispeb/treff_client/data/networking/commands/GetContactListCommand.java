package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 16.02.2018.
 */

public class GetContactListCommand extends AbstractCommand {

    private Request output;

    public GetContactListCommand(String token) {
        super(Response.class);
        output = new Request(token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO handle response
    }

    public static class Request extends AbstractRequest {

        public final String token;

        public Request(String token) {
            super("get-contact-list");
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int[] contacts;

        @JsonProperty("incoming-requests")
        public final int[] incomingRequests;

        @JsonProperty("outgoing-requests")
        public final int[] outgoingRequests;

        public Response(@JsonProperty("contacts") int[] contacts,
                        @JsonProperty("incoming-requests")
                                int[] incomingRequests,
                        @JsonProperty("outgoing-requests")
                                int[] outgoingRequests) {
            this.contacts = contacts;
            this.incomingRequests = incomingRequests;
            this.outgoingRequests = outgoingRequests;
        }
    }
}
