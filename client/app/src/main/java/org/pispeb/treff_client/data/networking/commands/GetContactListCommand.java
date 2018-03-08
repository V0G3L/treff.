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

    private final Location l = new Location(LocationManager.GPS_PROVIDER);

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
            checkUser( new User(c, "", true, false, false, false, l));
        }
        for (int in : response.incomingRequests) {
            checkUser( new User(in, "", false, false, true, false, l));
        }
        for (int out : response.outgoingRequests) {
            checkUser( new User(out, "", false, false, false, true, l));
        }
        for (int b : response.blocks) {
            checkUser( new User(b, "", false, true, false, false, l));
        }
    }

    private void checkUser(User u) {
        if (userRepository.getUser(u.getUserId()) == null) {
            encoder.getUserDetails(u.getUserId());
            Location l = new Location(LocationManager.GPS_PROVIDER);
            userRepository.addUser(u);
        } else {
            userRepository.setIsFriend(u.getUserId(), u.isFriend());
            userRepository.setIsRequesting(u.getUserId(), u.isRequesting());
            userRepository.setIsPending(u.getUserId(), u.isRequestPending());
            userRepository.setIsBlocked(u.getUserId(), u.isBlocked());
        }
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
