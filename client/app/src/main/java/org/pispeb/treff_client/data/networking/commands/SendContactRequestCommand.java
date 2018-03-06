package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Class representing a JSON send-contact-request object
 */

public class SendContactRequestCommand extends AbstractCommand{

    private Request output;
    private UserRepository userRepository;
    private String username;
    private String token;

    public SendContactRequestCommand(int id, String token, UserRepository userRepository) {
        super(Response.class);
        this.userRepository = userRepository;
        output = new Request(id, token);
    }

    public SendContactRequestCommand(String username, String token,
                                     UserRepository userRepository) {
        super(Response.class);
        this.userRepository = userRepository;
        this.username = username;
        this.token = token;
    }

    @Override
    public Request getRequest() {
        if (output == null) {
            User user = userRepository.getUser(username);
            output =  new Request(user.getUserId(), token);
        }
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userRepository.setIsPending(output.id, true);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.SEND_CONTACT_REQUEST.toString());
            this.id = id;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }

}
