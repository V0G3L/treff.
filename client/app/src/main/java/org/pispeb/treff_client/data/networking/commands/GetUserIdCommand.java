package org.pispeb.treff_client.data.networking.commands;

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
        // TODO different constructor
//        userRepository.add(new User(response.id, output.user));
        userRepository.add(new User(output.user, false, false, null));
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

        public Response(int id) {
            this.id = id;
        }
    }
}
