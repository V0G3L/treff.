package org.pispeb.treff_client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class GetContactListCommand extends AbstractCommand {

    private Request output;
    private UserRepository userRepository;
    private final RequestEncoder encoder;

    public GetContactListCommand(String token, UserRepository userRepository,
                                 RequestEncoder encoder) {
        super(Response.class);
        output = new Request(token);
        this.userRepository = userRepository;
        this.encoder = encoder;
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
            checkUser(c);
            userRepository.setIsFriend(c, true);
        }
        for (int in : response.incomingRequests) {
            checkUser(in);
            userRepository.setIsRequesting(in, true);
        }
        for (int out : response.outgoingRequests) {
            checkUser(out);
            userRepository.setIsPending(out, true);
        }
        for (int b : response.blocks) {
            checkUser(b);
            userRepository.setIsBlocked(b, true);
        }
    }

    private void checkUser(int id) {
        if (userRepository.getUserLiveData(id) != null) {
            encoder.getUserDetails(id);
            userRepository.addUser(newUser(id));
        }
    }

    private User newUser(int id) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        return new User(id, "", false, false, false, false, l);
    }

    public static class Request extends AbstractRequest {

        public final String token;

        public Request(String token) {
            super(CmdDesc.GET_CONTACT_LIST.toString());
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final int[] contacts;
        public final int[] incomingRequests;
        public final int[] outgoingRequests;
        public final int[] blocks;

        public Response(@JsonProperty("contacts")
                                int[] contacts,
                        @JsonProperty("incoming-requests")
                                int[] incomingRequests,
                        @JsonProperty("outgoing-requests")
                                int[] outgoingRequests,
                        @JsonProperty("blocks")
                                int[] blocks) {
            this.contacts = contacts;
            this.incomingRequests = incomingRequests;
            this.outgoingRequests = outgoingRequests;
            this.blocks = blocks;
        }
    }
}
