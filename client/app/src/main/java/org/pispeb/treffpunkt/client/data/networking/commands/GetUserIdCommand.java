package org.pispeb.treffpunkt.client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

/**
 * Class representing a  JSON get-user-id object
 * Requests the id of a user from the server and then triggers a contact request
 */

public class GetUserIdCommand extends AbstractCommand {

    private final String username;
    private final UserRepository userRepository;
    private Request output;
    private final RequestEncoder encoder;

    public GetUserIdCommand(String user, String token,
                            UserRepository userRepository,
                            RequestEncoder encoder) {
        super(Response.class);
        this.username = user;
        output = new Request(user, token);
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
        if (userRepository.getUser(response.id) == null)
            User.getPlaceholderAndScheduleQuery(response.id, userRepository,
                    (user) -> user.setUsername(username));
        encoder.sendContactRequest(response.id);
    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String token;

        public Request(String user, String token) {
            super(CmdDesc.GET_USER_ID.toString());
            this.user = user;
            this.token = token;
        }
    }

    public static class Response extends AbstractResponse {
        public final int id;
        public Response(@JsonProperty("id") int id) {
            this.id = id;
        }
    }
}
