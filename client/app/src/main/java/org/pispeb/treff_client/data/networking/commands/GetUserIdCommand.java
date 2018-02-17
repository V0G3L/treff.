package org.pispeb.treff_client.data.networking.commands;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Class representing a  JSON get-user-id object
 */

public class GetUserIdCommand extends AbstractCommand {

    private UserRepository userRepository;
    private Request output;

    public GetUserIdCommand(String user, String token,
                            UserRepository userRepository) {
        super(Response.class);
        output = new Request(user, token);
        this.userRepository = userRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userRepository.addUser(new User(
                response.id,
                output.user,
                true,
                false,
                new Location(LocationManager.GPS_PROVIDER)));
    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String token;

        public Request(String user, String token) {
            super("get-user-id");
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
