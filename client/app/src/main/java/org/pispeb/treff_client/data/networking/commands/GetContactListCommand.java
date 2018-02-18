package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class GetContactListCommand extends AbstractCommand {

    private Request output;
    private UserRepository userRepository;

    public GetContactListCommand(String token, UserRepository userRepository) {
        super(Response.class);
        output = new Request(token);
        this.userRepository = userRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        //Reset information about local users, replace with new Data from Server
        userRepository.resetAllUsers();

        for (int c : response.contacts) {
            userRepository.setIsFriend(c, true);
        }
        for (int in : response.incomingRequests) {
            userRepository.setIsRequesting(in, true);
        }
        for (int out : response.outgoingRequests) {
            userRepository.setIsPending(out, true);
        }
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
        public final int[] incomingRequests;
        public final int[] outgoingRequests;

        public Response(@JsonProperty("contacts") int[] contacts,
                        @JsonProperty("incoming-requests") int[] incomingRequests,
                        @JsonProperty("outgoing-requests") int[] outgoingRequests) {
            this.contacts = contacts;
            this.incomingRequests = incomingRequests;
            this.outgoingRequests = outgoingRequests;
        }
    }
}
